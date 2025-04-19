package com.alvarocervantes.fittrackplus.ui.fragments.routine

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel

class CreateRoutineFragment : Fragment() {

    private val viewModel: RoutineViewModel by viewModels()
    private val dayLayouts = mutableListOf<View>()


    private var routineName: String = ""
    private var numberOfDays: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routineName = it.getString("routineName") ?: ""
            numberOfDays = it.getInt("numberOfDays", 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_routine, container, false)
        val container = view.findViewById<LinearLayout>(R.id.containerRoutineDays)

        for (i in 1..numberOfDays) {
            val dayLayout = layoutInflater.inflate(R.layout.item_routine_day, container, false)
            dayLayouts.add(dayLayout)
            val editDayName = dayLayout.findViewById<EditText>(R.id.editTextDayName)
            val buttonAddExercise = dayLayout.findViewById<Button>(R.id.buttonAddExercise)
            val containerExercises = dayLayout.findViewById<LinearLayout>(R.id.containerExercises)


            editDayName.hint = "Sesión $i"

            buttonAddExercise.setOnClickListener {
                val exerciseInput = layoutInflater.inflate(R.layout.item_exercise_input, container, false)
                containerExercises.addView(exerciseInput)
            }

            container.addView(dayLayout)
        }
        val buttonSave = view.findViewById<Button>(R.id.buttonSaveRoutine)

        Log.d("RoutineDebug", "Total días en pantalla: ${container.childCount}")

        buttonSave.setOnClickListener {
            val daysList = mutableListOf<Pair<String, List<Triple<String, Int, String>>>>()

            for ((i, dayView) in dayLayouts.withIndex()) {

                val dayNameInput = dayView.findViewById<EditText?>(R.id.editTextDayName)
                val exerciseContainer = dayView.findViewById<LinearLayout?>(R.id.containerExercises)

                /**
                 * logs
                 */
                val containerExercises = dayView.findViewById<LinearLayout?>(R.id.containerExercises)
                if (containerExercises == null) {
                    Log.e("RoutineDebug", "El día $i no contiene containerExercises")
                    Log.d("RoutineDebug", "Vista actual: $dayView")
                    continue
                }

                if (exerciseContainer == null) {
                    Log.e("RoutineSave", "❌ containerExercises es null en el día $i")
                    continue
                }

                val dayName = dayNameInput?.text?.toString()?.ifBlank { "Sesión ${i + 1}" } ?: "Sesión ${i + 1}"
                val exercises = mutableListOf<Triple<String, Int, String>>()

                for (j in 0 until exerciseContainer.childCount) {
                    val exView = exerciseContainer.getChildAt(j)

                    val nameInput = exView.findViewById<EditText?>(R.id.editTextExerciseName)
                    val seriesInput = exView.findViewById<EditText?>(R.id.editTextSeries)
                    val repsInput = exView.findViewById<EditText?>(R.id.editTextReps)

                    if (nameInput == null || seriesInput == null || repsInput == null) {
                        Log.e("RoutineSave", "❌ Campo null en ejercicio $j del día $i")
                        continue
                    }

                    val name = nameInput.text.toString()
                    val series = seriesInput.text.toString().toIntOrNull() ?: 0
                    val reps = repsInput.text.toString()

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
                findNavController().navigate(R.id.routineListFragment) //Volver a addTraining
            } else {
                Log.w("RoutineSave", "⚠️ No se añadieron ejercicios válidos")
            }
        }

        return view
    }

}
