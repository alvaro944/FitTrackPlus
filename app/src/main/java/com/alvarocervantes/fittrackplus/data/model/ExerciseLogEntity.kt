package com.alvarocervantes.fittrackplus.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_logs")
data class ExerciseLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,       // ID de la sesión a la que pertenece
    val exerciseId: Long,      // ID del ejercicio definido
    @ColumnInfo(name="exerciseName") val exerciseName: String,
    val seriesNumber: Int,     // Número de serie (1, 2, 3…)
    val weight: Float,         // Peso levantado
    val repetitions: Int,      // Repeticiones realizadas
    val comment: String? = null // Comentario opcional
)

