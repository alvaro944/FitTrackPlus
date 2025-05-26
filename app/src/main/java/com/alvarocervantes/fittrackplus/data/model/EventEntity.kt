package com.alvarocervantes.fittrackplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // formato ISO yyyy-MM-dd
    val title: String
)