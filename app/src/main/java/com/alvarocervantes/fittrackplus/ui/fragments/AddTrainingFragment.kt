package com.alvarocervantes.fittrackplus.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.ui.adapters.RoutineAdapter
import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel
import kotlinx.coroutines.launch

class AddTrainingFragment : Fragment() {

    private val viewModel: RoutineViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_training, container, false)

        val buttonCreate = view.findViewById<Button>(R.id.buttonCreateRoutine)
        recyclerView = view.findViewById(R.id.recyclerRoutines)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        buttonCreate.setOnClickListener {
            findNavController().navigate(R.id.createRoutineStartFragment)
        }

        //Botón para registrar entrenamiento
        val buttonIrARegistrar = view.findViewById<Button>(R.id.buttonIrARegistrarEntreno)
        buttonIrARegistrar.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("fittrack_prefs", android.content.Context.MODE_PRIVATE)
            val lastRoutineId = prefs.getLong("last_gym_routine_id", -1L)

            if (lastRoutineId != -1L) {
                val bundle = Bundle().apply {
                    putLong("routineId", lastRoutineId)
                }
                findNavController().navigate(R.id.registerTrainingFragment, bundle)
            } else {
                Toast.makeText(requireContext(), "Selecciona una rutina primero", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar las rutinas desde Room
        lifecycleScope.launch {
            val routines = viewModel.getRoutinesWithDays()
            adapter = RoutineAdapter(
                routines,
                onRoutineClick = { routine ->
                    // Seleccionar rutina para entrenamiento
                    val prefs = requireContext().getSharedPreferences("fittrack_prefs", android.content.Context.MODE_PRIVATE)
                    prefs.edit().putLong("last_gym_routine_id", routine.id).apply()

                    val bundle = Bundle().apply {
                        putLong("routineId", routine.id)
                    }
                    findNavController().navigate(R.id.registerTrainingFragment, bundle)
                },
                onEditClick = { routine ->
                    // Más adelante: lógica para editar rutina
                    Toast.makeText(requireContext(), "Editar rutina (pendiente)", Toast.LENGTH_SHORT).show()
                }
            )
            recyclerView.adapter = adapter
        }

        return view
    }
    private fun setLastGymRoutineId(routineId: Long) {
        val prefs = requireContext().getSharedPreferences("fittrack_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putLong("last_gym_routine_id", routineId).apply()
    }

}
