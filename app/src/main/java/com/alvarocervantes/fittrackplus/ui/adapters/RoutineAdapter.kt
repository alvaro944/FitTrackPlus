package com.alvarocervantes.fittrackplus.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvarocervantes.fittrackplus.data.preferences.LastRoutineManager
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity

class RoutineAdapter(
    private val routines: List<Pair<RoutineEntity, Int>>, // Rutina y número de días
    private val onRoutineClick: (RoutineEntity) -> Unit,
    private val onEditClick: (RoutineEntity) -> Unit

) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textRoutineName)
        val textDays: TextView = view.findViewById(R.id.textRoutineDays)
        val buttonEdit: Button = view.findViewById(R.id.buttonEditRoutine)
        val cardView: View = view.findViewById(R.id.cardRoutine) // Referencia al CardView completo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine_card, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val (routine, dayCount) = routines[position]
        holder.textName.text = routine.name
        holder.textDays.text = "$dayCount ${if (dayCount == 1) "día" else "días"}"

        holder.cardView.setOnClickListener {
            LastRoutineManager.saveLastRoutineId(holder.itemView.context, routine.id)  // << NUEVO
            onEditClick(routine) // ya tenías esto
        }

        holder.buttonEdit.setOnClickListener {
            onEditClick(routine)
        }
    }



    override fun getItemCount(): Int = routines.size
}
