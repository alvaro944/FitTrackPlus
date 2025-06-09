package com.alvarocervantes.fittrackplus.ui.fragments.training

import android.os.Bundle
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
import com.alvarocervantes.fittrackplus.viewmodel.RegisterTrainingViewModel
import com.alvarocervantes.fittrackplus.utils.setSafeClickListener
import kotlinx.coroutines.launch

class RegisterTrainingFragment : Fragment() {

    private val viewModel: RegisterTrainingViewModel by viewModels()
    private var routineId: Long = -1L
    private lateinit var containerLayout: LinearLayout
    private lateinit var buttonSave: Button

    data class SerieInput(
        val exerciseId: Long,
        val exerciseName: String,
        val seriesNumber: Int,
        val pesoEdit: EditText,
        val repsEdit: EditText
    )

    private val inputSeries = mutableListOf<SerieInput>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        routineId = arguments?.getLong("routineId", -1L) ?: -1L

        // Si no se pasa rutinaId, lo buscamos en SharedPreferences
        if (routineId == -1L) {
            val prefs = requireContext().getSharedPreferences("fittrack_prefs", android.content.Context.MODE_PRIVATE)
            routineId = prefs.getLong("last_gym_routine_id", -1L)
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
                    val textRoutineTitle = view.findViewById<TextView>(R.id.textRoutineTitle)
                    val textSessionTitle = view.findViewById<TextView>(R.id.textSessionTitle)
                    val headerContainer = view.findViewById<LinearLayout>(R.id.containerTrainingHeader)

                    headerContainer.visibility = View.VISIBLE
                    textRoutineTitle.text = "Entrenando: ${routine.name}"

                    // Calcular número de semana
                    val sessions = viewModel.getSessionsForRoutine(routineId)
                    val sessionsOfThisDay = sessions.count { it.dayId == day.id }
                    val weekNumber = sessionsOfThisDay + 1

                    textSessionTitle.text = "Sesión: ${day.dayName} - Semana $weekNumber"
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

                        repeat(exercise.series) { index ->
                            val serieView = layoutInflater.inflate(R.layout.item_serie_input, containerSeries, false)
                            val pesoEdit = serieView.findViewById<EditText>(R.id.editPeso)
                            val repsEdit = serieView.findViewById<EditText>(R.id.editReps)

                            inputSeries.add(
                                SerieInput(
                                    exercise.id,
                                    exercise.name,
                                    index + 1,
                                    pesoEdit,
                                    repsEdit
                                )
                            )
                            containerSeries.addView(serieView)
                        }

                        containerLayout.addView(exerciseInput)
                    }

                    buttonSave.setSafeClickListener {
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

            inputSeries.forEach { (exerciseId, exerciseName, seriesNumber, pesoEdit, repsEdit) ->
                val peso = pesoEdit.text.toString().toFloatOrNull() ?: 0f
                val reps = repsEdit.text.toString().toIntOrNull() ?: 0

                if (peso > 0 && reps > 0) {
                    viewModel.insertLog(
                        ExerciseLogEntity(
                            sessionId = sessionId,
                            exerciseId = exerciseId,
                            exerciseName = exerciseName,
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
}