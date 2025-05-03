package com.alvarocervantes.fittrackplus.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.data.preferences.LastRoutineManager

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val buttonViewHistory = view.findViewById<Button>(R.id.buttonViewHistory)

        buttonViewHistory.setOnClickListener {
            val lastRoutineId = LastRoutineManager.getLastRoutineId(requireContext())
            if (lastRoutineId != -1L) {
                val bundle = Bundle().apply {
                    putLong("routineId", lastRoutineId)
                }
                findNavController().navigate(R.id.trainingHistoryFragment, bundle)
            } else {
                Toast.makeText(requireContext(), "❗Selecciona una rutina primero", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

