package com.alvarocervantes.fittrackplus.ui.fragments.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.viewmodel.TrainingHistoryViewModel
import kotlinx.coroutines.launch

class TrainingHistoryFragment : Fragment() {

    private val viewModel: TrainingHistoryViewModel by viewModels()
    private lateinit var containerHistory: LinearLayout
    private lateinit var textTitle: TextView

    private var routineId: Long = -1L   // Añadido para recibir la rutina

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
        return inflater.inflate(R.layout.fragment_training_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerHistory = view.findViewById(R.id.containerTrainingHistory)
        textTitle = view.findViewById(R.id.textRoutineTitle)

        viewModel.setRoutineId(routineId)   // Le pasamos la rutina al ViewModel

        cargarHistorial()
    }

    private fun cargarHistorial() {
        lifecycleScope.launch {
            val sessionsWithExercises = viewModel.getTrainingHistory()

            textTitle.text = viewModel.getRoutineNameForCurrentSession()

            if (sessionsWithExercises.isEmpty()) {
                val emptyText = TextView(requireContext()).apply {
                    text = "No hay entrenamientos registrados todavía."
                    textSize = 16f
                    setPadding(16, 32, 16, 32)
                }
                containerHistory.addView(emptyText)
            } else {
                sessionsWithExercises.forEach { (session, exercises) ->
                    val sessionCard = layoutInflater.inflate(
                        R.layout.item_training_history_session,
                        containerHistory,
                        false
                    )

                    val textSessionHeader = sessionCard.findViewById<TextView>(R.id.textSessionHeader)
                    val containerExercises = sessionCard.findViewById<LinearLayout>(R.id.containerExercises)
                    val dayName = viewModel.getDayName(session.dayId) ?: "Desconocido"

                    textSessionHeader.text = "📅 ${session.date} - 🧠 $dayName"

                    exercises.forEach { log ->
                        val exerciseView = layoutInflater.inflate(
                            R.layout.item_serie_logged,
                            containerExercises,
                            false
                        )

                        val textSerieIndex = exerciseView.findViewById<TextView>(R.id.textSerieIndex)
                        val textPeso = exerciseView.findViewById<TextView>(R.id.textPeso)
                        val textReps = exerciseView.findViewById<TextView>(R.id.textReps)

                        textSerieIndex.text = "Serie ${log.seriesNumber}"
                        textPeso.text = "${log.weight} kg"
                        textReps.text = "${log.repetitions} reps"

                        containerExercises.addView(exerciseView)
                    }

                    containerHistory.addView(sessionCard)
                }
            }
        }
    }
}

