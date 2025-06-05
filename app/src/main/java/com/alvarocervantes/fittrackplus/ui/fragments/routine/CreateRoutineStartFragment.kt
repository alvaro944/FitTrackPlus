package com.alvarocervantes.fittrackplus.ui.fragments.routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CreateRoutineStartFragment : Fragment() {

    private var selectedDays = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_routine_start, container, false)

        val inputLayout = view.findViewById<TextInputLayout>(R.id.routineNameLayout)
        val editTitle = view.findViewById<TextInputEditText>(R.id.editTextRoutineName)
        val slider = view.findViewById<Slider>(R.id.sliderDays)
        val textDays = view.findViewById<TextView>(R.id.textDaysSelected)
        val buttonIncrease = view.findViewById<MaterialButton>(R.id.buttonIncrease)
        val buttonDecrease = view.findViewById<MaterialButton>(R.id.buttonDecrease)
        val buttonContinue = view.findViewById<MaterialButton>(R.id.buttonContinue)
        val buttonCancel = view.findViewById<MaterialButton>(R.id.buttonCancel)

        // Inicializar texto
        textDays.text = selectedDays.toString()
        slider.value = selectedDays.toFloat()

        // Sincronizar slider → texto y variable
        slider.addOnChangeListener { _, value, _ ->
            selectedDays = value.toInt()
            textDays.text = selectedDays.toString()
        }

        // Botón de aumentar días
        buttonIncrease.setOnClickListener {
            if (selectedDays < 30) {
                selectedDays++
                slider.value = selectedDays.toFloat()
                textDays.text = selectedDays.toString()
            }
        }

        // Botón de disminuir días
        buttonDecrease.setOnClickListener {
            if (selectedDays > 1) {
                selectedDays--
                slider.value = selectedDays.toFloat()
                textDays.text = selectedDays.toString()
            }
        }

        // Continuar
        buttonContinue.setOnClickListener {
            val name = editTitle.text.toString().trim()

            if (name.isEmpty()) {
                inputLayout.error = "⚠️ Introduce un nombre para la rutina"
            } else {
                inputLayout.error = null
                val bundle = Bundle().apply {
                    putString("routineName", name)
                    putInt("numberOfDays", selectedDays)
                }
                findNavController().navigate(R.id.createRoutineFragment, bundle)
            }
        }

        // Cancelar
        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }
}