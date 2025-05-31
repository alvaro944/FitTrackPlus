package com.alvarocervantes.fittrackplus.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R

class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        val button = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGoToTestMaterial)
        button.setOnClickListener {
        }

        return view
    }
}