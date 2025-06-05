package com.alvarocervantes.fittrackplus.ui.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity
import com.alvarocervantes.fittrackplus.data.preferences.LastRoutineManager

class RoutineAdapter(
    private val routines: List<Pair<RoutineEntity, Int>>,
    private val onRoutineClick: (RoutineEntity) -> Unit,
    private val onEditClick: (RoutineEntity) -> Unit,
    private val onDeleteClick: (RoutineEntity) -> Unit
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textRoutineName)
        val textDays: TextView = view.findViewById(R.id.textRoutineDays)
        val buttonEdit: Button = view.findViewById(R.id.buttonEditRoutine)
        val buttonDelete: Button = view.findViewById(R.id.buttonDeleteRoutine)
        val cardView: View = view.findViewById(R.id.cardRoutine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine_card, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val (routine, dayCount) = routines[position]
        val context = holder.itemView.context

        holder.textName.text = routine.name
        holder.textDays.text = "$dayCount ${if (dayCount == 1) "día" else "días"}"

        val isSelected = LastRoutineManager.isRoutineSelected(context, routine.id)

        // Cambiar estilo visual si está seleccionada
        holder.textName.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        holder.textName.textSize = if (isSelected) 18f else 16f

        // 🟠 Borde de la tarjeta
        if (holder.cardView is com.google.android.material.card.MaterialCardView) {
            val card = holder.cardView as com.google.android.material.card.MaterialCardView
            card.strokeColor = if (isSelected)
                context.getColor(R.color.acento) // secundario si está seleccionada
            else
                context.getColor(R.color.primario)
        }


        holder.cardView.setOnClickListener {
            if (isSelected) {
                onRoutineClick(routine) // Ya seleccionada → abrir entrenamiento
            } else {
                LastRoutineManager.saveLastRoutineId(context, routine.id)
                notifyDataSetChanged() // Redibuja para actualizar visual
            }
        }

        holder.buttonEdit.setOnClickListener {
            onEditClick(routine)
        }
        holder.buttonDelete.setOnClickListener {
            onDeleteClick(routine)
        }
    }


    override fun getItemCount(): Int = routines.size
}