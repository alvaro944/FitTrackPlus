package com.alvarocervantes.fittrackplus.data.dao

import androidx.room.*
import com.alvarocervantes.fittrackplus.data.model.CalendarEventEntity

@Dao
interface CalendarEventDao {

    @Insert
    suspend fun insertEvent(event: CalendarEventEntity): Long

    @Query("SELECT * FROM calendar_events WHERE date = :date")
    suspend fun getEventsByDate(date: String): List<CalendarEventEntity>

    @Delete
    suspend fun deleteEvent(event: CalendarEventEntity)
}