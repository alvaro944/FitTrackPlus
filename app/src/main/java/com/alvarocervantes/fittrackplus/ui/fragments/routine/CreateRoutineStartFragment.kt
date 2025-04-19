package com.alvarocervantes.fittrackplus.ui.fragments.routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R

class CreateRoutineStartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_routine_start, container, false)

        val editTitle = view.findViewById<EditText>(R.id.editTextRoutineName)
        val numberPickerDays = view.findViewById<NumberPicker>(R.id.numberPickerDays)
        val buttonContinue = view.findViewById<Button>(R.id.buttonContinue)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)

        numberPickerDays.minValue = 1
        numberPickerDays.maxValue = 7

        buttonContinue.setOnClickListener {
            val name = editTitle.text.toString().trim()
            val days = numberPickerDays.value

            if (name.isNotEmpty()) {
                val bundle = Bundle().apply {
                    putString("routineName", name)
                    putInt("numberOfDays", days)
                }

                findNavController().navigate(R.id.createRoutineFragment, bundle)
            }
        }

        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }
}
