package com.alvarocervantes.fittrackplus.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider
import com.alvarocervantes.fittrackplus.data.model.CalendarEventEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var adapter: ArrayAdapter<String>
    private val eventTitles = mutableListOf<String>()

    private var selectedDate: String = getToday()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        listView = view.findViewById(R.id.listEvents)
        addButton = view.findViewById(R.id.buttonAddEvent)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, eventTitles)
        listView.adapter = adapter

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
            cargarEventosDelDia()
        }

        addButton.setOnClickListener {
            mostrarDialogoCrearEvento()
        }

        cargarEventosDelDia()

        return view
    }

    private fun cargarEventosDelDia() {
        lifecycleScope.launch {
            val dao = DatabaseProvider.getDatabase(requireContext()).calendarEventDao()
            val eventos = dao.getEventsByDate(selectedDate)

            eventTitles.clear()
            eventTitles.addAll(eventos.map { it.title })
            adapter.notifyDataSetChanged()
        }
    }

    private fun mostrarDialogoCrearEvento() {
        val input = EditText(requireContext()).apply {
            hint = "Título del evento"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Nuevo evento")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = input.text.toString()
                if (titulo.isNotBlank()) {
                    guardarEvento(titulo)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarEvento(titulo: String) {
        val evento = CalendarEventEntity(date = selectedDate, title = titulo)
        lifecycleScope.launch {
            val dao = DatabaseProvider.getDatabase(requireContext()).calendarEventDao()
            dao.insertEvent(evento)
            cargarEventosDelDia()
            Toast.makeText(requireContext(), "Evento guardado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getToday(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(Date())
    }
}