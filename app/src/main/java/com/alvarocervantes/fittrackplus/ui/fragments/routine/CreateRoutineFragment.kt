package com.alvarocervantes.fittrackplus.ui.fragments.routine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel
import kotlinx.coroutines.launch
import com.alvarocervantes.fittrackplus.data.dao.RoutineDao
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity
import com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity
import com.alvarocervantes.fittrackplus.data.model.ExerciseEntity


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_routine, container, false)
        val containerLayout = view.findViewById<LinearLayout>(R.id.containerRoutineDays)

        val buttonSave = view.findViewById<Button>(R.id.buttonSaveRoutine)

        if (routineIdToEdit != -1L) {
            // EDITAR RUTINA
            lifecycleScope.launch {
                val routine = viewModel.getRoutineById(routineIdToEdit)
                val days = viewModel.getDaysForRoutine(routineIdToEdit)

                routineName = routine?.name ?: routineName
                numberOfDays = days.size

                for ((index, day) in days.withIndex()) {
                    val dayLayout = layoutInflater.inflate(R.layout.item_routine_day, containerLayout, false)
                    dayLayouts.add(dayLayout)

                    val editDayName = dayLayout.findViewById<EditText>(R.id.editTextDayName)
                    val buttonAddExercise = dayLayout.findViewById<Button>(R.id.buttonAddExercise)
                    val containerExercises = dayLayout.findViewById<LinearLayout>(R.id.containerExercises)

                    editDayName.setText(day.dayName)

                    buttonAddExercise.setOnClickListener {
                        val exerciseInput = layoutInflater.inflate(R.layout.item_exercise_input, containerLayout, false)
                        containerExercises.addView(exerciseInput)
                    }

                    containerLayout.addView(dayLayout)

                    val exercises = viewModel.getExercisesForDay(day.id)
                    for (exercise in exercises) {
                        val exerciseInput = layoutInflater.inflate(R.layout.item_exercise_input, containerLayout, false)
                        val nameInput = exerciseInput.findViewById<EditText>(R.id.editTextExerciseName)
                        val seriesInput = exerciseInput.findViewById<EditText>(R.id.editTextSeries)
                        val repsInput = exerciseInput.findViewById<EditText>(R.id.editTextReps)

                        nameInput.setText(exercise.name)
                        seriesInput.setText(exercise.series.toString())
                        repsInput.setText(exercise.reps)

                        containerExercises.addView(exerciseInput)
                    }
                }
            }
        } else {
            // NUEVA RUTINA
            for (i in 1..numberOfDays) {
                val dayLayout = layoutInflater.inflate(R.layout.item_routine_day, containerLayout, false)
                dayLayouts.add(dayLayout)

                val editDayName = dayLayout.findViewById<EditText>(R.id.editTextDayName)
                val buttonAddExercise = dayLayout.findViewById<Button>(R.id.buttonAddExercise)
                val containerExercises = dayLayout.findViewById<LinearLayout>(R.id.containerExercises)

                editDayName.hint = "Sesión $i"

                buttonAddExercise.setOnClickListener {
                    val exerciseInput = layoutInflater.inflate(R.layout.item_exercise_input, containerLayout, false)
                    containerExercises.addView(exerciseInput)
                }

                containerLayout.addView(dayLayout)
            }
        }

        buttonSave.setOnClickListener {
            val daysList = mutableListOf<Pair<String, List<Triple<String, Int, String>>>>()

            for ((i, dayView) in dayLayouts.withIndex()) {
                val dayNameInput = dayView.findViewById<EditText?>(R.id.editTextDayName)
                val exerciseContainer = dayView.findViewById<LinearLayout?>(R.id.containerExercises)

                val dayName = dayNameInput?.text?.toString()?.ifBlank { "Sesión ${i + 1}" } ?: "Sesión ${i + 1}"
                val exercises = mutableListOf<Triple<String, Int, String>>()

                for (j in 0 until exerciseContainer!!.childCount) {
                    val exView = exerciseContainer.getChildAt(j)

                    val nameInput = exView.findViewById<EditText?>(R.id.editTextExerciseName)
                    val seriesInput = exView.findViewById<EditText?>(R.id.editTextSeries)
                    val repsInput = exView.findViewById<EditText?>(R.id.editTextReps)

                    val name = nameInput?.text.toString()
                    val series = seriesInput?.text.toString().toIntOrNull() ?: 0
                    val reps = repsInput?.text.toString()

                    if (name.isNotBlank() && series > 0 && reps.isNotBlank()) {
                        exercises.add(Triple(name, series, reps))
                    }
                }

                if (exercises.isNotEmpty()) {
                    daysList.add(dayName to exercises)
                }
            }

            if (daysList.isNotEmpty()) {
                viewModel.insertRoutineWithDaysAndExercises(routineName, daysList)
                findNavController().popBackStack(R.id.addTrainingFragment, false)
            } else {
                Toast.makeText(requireContext(), "⚠️ No se añadieron ejercicios válidos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

