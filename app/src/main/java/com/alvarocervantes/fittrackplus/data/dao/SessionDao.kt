package com.alvarocervantes.fittrackplus.data.dao

import androidx.room.*
import com.alvarocervantes.fittrackplus.data.model.SessionEntity
import com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity
import com.alvarocervantes.fittrackplus.data.model.ExerciseLogWithName

@Dao
interface SessionDao {

    // Sesiones de entrenamiento
    @Insert suspend fun insertSession(session: SessionEntity): Long
    @Query("SELECT id, routineId, dayId, date, week, dayName, comment FROM sessions ORDER BY date DESC")
    suspend fun getAllSessions(): List<SessionEntity>
    @Query("SELECT id, routineId, dayId, date, week, dayName, comment FROM sessions WHERE routineId = :routineId ORDER BY date DESC")
    suspend fun getSessionsByRoutine(routineId: Long): List<SessionEntity>

    // Logs de ejercicios (registro de cada serie)
    @Insert suspend fun insertExerciseLog(log: ExerciseLogEntity): Long
    @Query("SELECT * FROM exercise_logs WHERE sessionId = :sessionId ORDER BY exerciseId, seriesNumber")
    suspend fun getLogsForSession(sessionId: Long): List<ExerciseLogEntity>

    @Query("""
    SELECT el.* FROM exercise_logs el
    INNER JOIN sessions s ON el.sessionId = s.id
    WHERE el.exerciseId = :exerciseId
    ORDER BY s.date DESC
""")
    suspend fun getLogsByExerciseOrdered(exerciseId: Long): List<ExerciseLogEntity>

    @Query("SELECT * FROM sessions WHERE routineId = :routineId ORDER BY date DESC")
    suspend fun getSessionsForRoutine(routineId: Long): List<SessionEntity>

    @Query("""
    SELECT el.*, e.name AS exercise_name
    FROM exercise_logs el
    INNER JOIN exercises e ON el.exerciseId = e.id
    WHERE el.sessionId = :sessionId
    ORDER BY el.exerciseId, el.seriesNumber
""")
    suspend fun getLogsWithNamesForSession(sessionId: Long): List<ExerciseLogWithName>

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Long): SessionEntity?

    @Query("SELECT * FROM sessions WHERE routineId = :routineId ORDER BY date DESC LIMIT 1")
    suspend fun getLastSessionOfRoutine(routineId: Long): SessionEntity?
}



