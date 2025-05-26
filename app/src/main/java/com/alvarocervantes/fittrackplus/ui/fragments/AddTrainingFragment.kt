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
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity
import com.alvarocervantes.fittrackplus.ui.adapters.RoutineAdapter
import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel
import kotlinx.coroutines.launch

class AddTrainingFragment : Fragment() {

    private val viewModel: RoutineViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoutineAdapter

    private val onRoutineClick: (RoutineEntity) -> Unit = { routine ->
        val prefs = requireContext().getSharedPreferences("fittrack_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putLong("last_gym_routine_id", routine.id).apply()

        val bundle = Bundle().apply {
            putLong("routineId", routine.id)
        }
        findNavController().navigate(R.id.registerTrainingFragment, bundle)
    }

    private val onEditClick: (RoutineEntity) -> Unit = { routine ->
        val action = AddTrainingFragmentDirections
            .actionAddTrainingFragmentToCreateRoutineFragment(routineIdToEdit = routine.id)
        findNavController().navigate(action)
    }

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

        return view
    }

    override fun onResume() {
        super.onResume()
        loadRoutines()
    }

    private fun loadRoutines() {
        lifecycleScope.launch {
            val routines = viewModel.getRoutinesWithDays()
            adapter = RoutineAdapter(
                routines,
                onRoutineClick = onRoutineClick,
                onEditClick = onEditClick,
                onDeleteClick = { routine ->
                    lifecycleScope.launch {
                        viewModel.deleteRoutine(routine)
                        loadRoutines() // ← refresca después de borrar
                        Toast.makeText(requireContext(), "Rutina eliminada", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            recyclerView.adapter = adapter
        }
    }

    private fun setLastGymRoutineId(routineId: Long) {
        val prefs = requireContext().getSharedPreferences("fittrack_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putLong("last_gym_routine_id", routineId).apply()
    }
}

