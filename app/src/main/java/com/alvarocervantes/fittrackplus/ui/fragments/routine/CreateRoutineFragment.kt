package com.alvarocervantes.fittrackplus.ui.fragments.routine

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel
import kotlinx.coroutines.launch

class CreateRoutineFragment : Fragment() {

    private val viewModel: RoutineViewModel by viewModels()
    private val dayLayouts = mutableListOf<View>()

    private var routineName: String = ""
    private var numberOfDays: Int = 1
    private var routineIdToEdit: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routineName = it.getString("routineName") ?: ""
            numberOfDays = it.getInt("numberOfDays", 1)
            routineIdToEdit = it.getLong("routineIdToEdit", -1L)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_create_routine, container, false)
        val containerLayout = view.findViewById<LinearLayout>(R.id.containerRoutineDays)
        val buttonSave = view.findViewById<Button>(R.id.buttonSaveRoutine)

        if (routineIdToEdit != -1L) {
            lifecycleScope.launch {
                val routine = viewModel.getRoutineById(routineIdToEdit)
                val days = viewModel.getDaysForRoutine(routineIdToEdit)

                routineName = routine?.name ?: routineName
                numberOfDays = days.size

                for ((index, day) in days.withIndex()) {
                    val dayLayout = createDayLayout(day.dayName)
                    containerLayout.addView(dayLayout)
                    dayLayouts.add(dayLayout)

                    val containerExercises = dayLayout.findViewById<LinearLayout>(R.id.containerExercises)
                    val exercises = viewModel.getExercisesForDay(day.id)

                    for (exercise in exercises) {
                        val exerciseInput = createExerciseInput(exercise.name, exercise.series.toString(), exercise.reps)
                        containerExercises.addView(exerciseInput)
                    }
                }
            }
        } else {
            for (i in 1..numberOfDays) {
                val dayLayout = createDayLayout("Sesión $i")
                containerLayout.addView(dayLayout)
                dayLayouts.add(dayLayout)
            }
        }

        buttonSave.setOnClickListener {
            val daysList = mutableListOf<Pair<String, List<Triple<String, Int, String>>>>()
            var firstInvalidField: View? = null

            for ((i, dayView) in dayLayouts.withIndex()) {
                val dayNameInput = dayView.findViewById<EditText>(R.id.editTextDayName)
                val exerciseContainer = dayView.findViewById<LinearLayout>(R.id.containerExercises)

                val dayName = dayNameInput.text.toString().ifBlank { "Sesión ${i + 1}" }
                val exercises = mutableListOf<Triple<String, Int, String>>()

                for (j in 0 until exerciseContainer.childCount) {
                    val exView = exerciseContainer.getChildAt(j)
                    val nameInput = exView.findViewById<EditText>(R.id.editTextExerciseName)
                    val seriesInput = exView.findViewById<EditText>(R.id.editTextSeries)
                    val repsInput = exView.findViewById<EditText>(R.id.editTextReps)

                    val name = nameInput.text.toString()
                    val series = seriesInput.text.toString().toIntOrNull() ?: 0
                    val reps = repsInput.text.toString()

                    val valid = name.isNotBlank() && series > 0 && reps.isNotBlank()

                    nameInput.setBackgroundResource(if (name.isBlank()) R.drawable.bg_edittext_error else android.R.color.transparent)
                    seriesInput.setBackgroundResource(if (series == 0) R.drawable.bg_edittext_error else android.R.color.transparent)
                    repsInput.setBackgroundResource(if (reps.isBlank()) R.drawable.bg_edittext_error else android.R.color.transparent)

                    if (!valid && firstInvalidField == null) {
                        firstInvalidField = exView
                    }

                    if (valid) {
                        exercises.add(Triple(name, series, reps))
                    }
                }

                if (exercises.isNotEmpty()) {
                    daysList.add(dayName to exercises)
                }
            }

            if (firstInvalidField != null) {
                firstInvalidField.requestFocus()
                Toast.makeText(requireContext(), "⚠️ Revisa los campos en rojo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (routineIdToEdit != -1L) {
                    viewModel.updateRoutineWithNewDaysAndExercises(routineIdToEdit, daysList)
                } else {
                    viewModel.insertRoutineWithDaysAndExercises(routineName, daysList)
                }
                findNavController().popBackStack(R.id.addTrainingFragment, false)
            }
        }

        return view
    }

    private fun createDayLayout(dayHint: String): View {
        val dayLayout = layoutInflater.inflate(R.layout.item_routine_day, null, false)
        val editDayName = dayLayout.findViewById<EditText>(R.id.editTextDayName)
        val buttonAddExercise = dayLayout.findViewById<Button>(R.id.buttonAddExercise)
        val containerExercises = dayLayout.findViewById<LinearLayout>(R.id.containerExercises)

        editDayName.hint = dayHint

        buttonAddExercise.setOnClickListener {
            val exerciseInput = createExerciseInput()
            containerExercises.addView(exerciseInput)
        }

        return dayLayout
    }

    private fun createExerciseInput(name: String = "", series: String = "", reps: String = ""): View {
        val exerciseInput = layoutInflater.inflate(R.layout.item_exercise_input, null, false)

        val nameInput = exerciseInput.findViewById<EditText>(R.id.editTextExerciseName)
        val seriesInput = exerciseInput.findViewById<EditText>(R.id.editTextSeries)
        val repsInput = exerciseInput.findViewById<EditText>(R.id.editTextReps)

        nameInput.setText(name)
        seriesInput.setText(series)
        repsInput.setText(reps)

        val buttonDelete = Button(requireContext()).apply {
            text = "Eliminar"
            setOnClickListener {
                val parent = exerciseInput.parent as? LinearLayout
                parent?.removeView(exerciseInput)
            }
        }

        (exerciseInput as ViewGroup).addView(buttonDelete)

        return exerciseInput
    }
}