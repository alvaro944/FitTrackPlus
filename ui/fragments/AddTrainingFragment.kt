package com.alvarocervantes.fittrackplus.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        // Cargar las rutinas desde Room
        lifecycleScope.launch {
            val routines = viewModel.getRoutinesWithDays()
            adapter = RoutineAdapter(routines) { routine ->
                // Acción de editar rutina (más adelante)
            }
            recyclerView.adapter = adapter
        }

        return view
    }
}
