package com.alvarocervantes.fittrackplus.ui.fragments.training

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity
import com.alvarocervantes.fittrackplus.data.model.SessionEntity
import com.alvarocervantes.fittrackplus.viewmodel.RegisterTrainingViewModel
import kotlinx.coroutines.launch

class RegisterTrainingFragment : Fragment() {

    private val viewModel: RegisterTrainingViewModel by viewModels()
    private var routineId: Long = -1L
    private lateinit var containerLayout: LinearLayout
    private lateinit var buttonSave: Button

    // Custom data class to hold inputs
    data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
    data class SerieInput(val exerciseId: Long, val seriesNumber: Int, val pesoEdit: EditText, val repsEdit: EditText)

    private val inputSeries = mutableListOf<SerieInput>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routineId = it.getLong("routineId", -1L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register_training, container, false)
        containerLayout = view.findViewById(R.id.containerRegisterTraining)
        buttonSave = view.findViewById(R.id.buttonSaveTraining)

        if (routineId == -1L) {
            Toast.makeText(requireContext(), "❌ Rutina no válida", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            lifecycleScope.launch {
                val day = viewModel.getNextRoutineDay(routineId)
                val routine = viewModel.getRoutineById(routineId)


                if (day != null && routine != null) {
                    val textHeader = view.findViewById<TextView>(R.id.textTrainingHeader)
                    textHeader.visibility = View.VISIBLE
                    textHeader.text = "Entrenando: ${routine.name} - ${day.dayName}"
                    Log.d("Registro", "➡️ Tocaría día: ${day.dayName}")
                }

                if (day != null) {
                    val exercises = viewModel.getExercisesForDay(day.id)

                    for (exercise in exercises) {
                        val exerciseInput = layoutInflater.inflate(
                            R.layout.item_exercise_log_input,
                            containerLayout,
                            false
                        )

                        val nameText = exerciseInput.findViewById<TextView>(R.id.textExerciseName)
                        val containerSeries = exerciseInput.findViewById<LinearLayout>(R.id.containerSeries)

                        nameText.text = exercise.name

                        // 🔁 Generar series automáticamente según las que definiste
                        repeat(exercise.series) { index ->
                            val serieView = layoutInflater.inflate(R.layout.item_serie_input, containerSeries, false)
                            val pesoEdit = serieView.findViewById<EditText>(R.id.editPeso)
                            val repsEdit = serieView.findViewById<EditText>(R.id.editReps)

                            inputSeries.add(SerieInput(exercise.id, index + 1, pesoEdit, repsEdit))
                            containerSeries.addView(serieView)
                        }

                        containerLayout.addView(exerciseInput)
                    }


                    buttonSave.setOnClickListener {
                        saveTraining(day.id)
                    }
                } else {
                    Toast.makeText(requireContext(), "⚠️ No hay días definidos para esta rutina", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }

        return view
    }

    private fun saveTraining(dayId: Long) {
        lifecycleScope.launch {
            val sessionId = viewModel.insertSessionWithWeek(routineId, dayId)

            Log.d("Registro", "✅ Sesión guardada: ID=$sessionId")

            inputSeries.forEach { (exerciseId, seriesNumber, pesoEdit, repsEdit) ->
                val peso = pesoEdit.text.toString().toFloatOrNull() ?: 0f
                val reps = repsEdit.text.toString().toIntOrNull() ?: 0

                if (peso > 0 && reps > 0) {
                    viewModel.insertLog(
                        ExerciseLogEntity(
                            sessionId = sessionId,
                            exerciseId = exerciseId,
                            seriesNumber = seriesNumber,
                            weight = peso,
                            repetitions = reps,
                            comment = null
                        )
                    )
                }
            }


            Toast.makeText(requireContext(), "✅ Entrenamiento guardado", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack(R.id.addTrainingFragment, false)
        }
    }

    private fun getCurrentDateIso(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val date = java.util.Date(currentTimeMillis)
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return format.format(date)
    }

}
