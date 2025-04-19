package com.alvarocervantes.fittrackplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_days")
data class RoutineDayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: Long,
    val dayName: String,
    val dayOrder: Int // para saber si es día 1, 2, 3, etc.
)
