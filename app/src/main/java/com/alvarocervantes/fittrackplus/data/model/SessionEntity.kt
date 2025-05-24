package com.alvarocervantes.fittrackplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: Long,        // A qué rutina pertenece
    val dayId: Long,            // A qué día de rutina corresponde
    val date: String,           // Fecha de la sesión (en formato ISO 8601)
    val week: Int,              // Semana de la rutina (vuelta)
    val dayName: String,         // Nombre dia de entrenamiento
    val comment: String? = null // Comentario opcional de la sesión
)

