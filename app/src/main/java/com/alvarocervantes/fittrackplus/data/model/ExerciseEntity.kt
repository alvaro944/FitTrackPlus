package com.alvarocervantes.fittrackplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayId: Long,         // ID del día de rutina al que pertenece
    val name: String,        // Nombre del ejercicio (Ej: Press Banca)
    val series: Int,          // Número de series definidas para este ejercicio
    val reps: String
)
