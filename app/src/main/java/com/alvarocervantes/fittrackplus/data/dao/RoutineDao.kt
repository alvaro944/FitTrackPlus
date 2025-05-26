package com.alvarocervantes.fittrackplus.data.dao

import androidx.room.*
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity
import com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity
import com.alvarocervantes.fittrackplus.data.model.ExerciseEntity

@Dao
interface RoutineDao {

    // Rutinas
    @Insert suspend fun insertRoutine(routine: RoutineEntity): Long
    @Query("SELECT * FROM routines") suspend fun getAllRoutines(): List<RoutineEntity>
    @Delete suspend fun deleteRoutine(routine: RoutineEntity)
    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity?
    @Query("UPDATE routines SET name = :newName WHERE id = :routineId")
    suspend fun updateRoutineName(routineId: Long, newName: String)

    // Días de rutina
    @Insert suspend fun insertDay(day: RoutineDayEntity): Long
    @Query("SELECT * FROM routine_days WHERE routineId = :routineId ORDER BY dayOrder")
    suspend fun getDaysForRoutine(routineId: Long): List<RoutineDayEntity>
    @Query("DELETE FROM routine_days WHERE routineId = :routineId")
    suspend fun deleteDaysForRoutine(routineId: Long)

    // Ejercicios definidos
    @Insert suspend fun insertExercise(exercise: ExerciseEntity): Long
    @Query("SELECT * FROM exercises WHERE dayId = :dayId")
    suspend fun getExercisesForDay(dayId: Long): List<ExerciseEntity>
    @Query("DELETE FROM exercises WHERE dayId = :dayId")
    suspend fun deleteExercisesForDay(dayId: Long)

    @Query("SELECT dayName FROM routine_days WHERE id = :dayId")
    suspend fun getDayNameById(dayId: Long): String?

    @Query("DELETE FROM exercises WHERE dayId IN (SELECT id FROM routine_days WHERE routineId = :routineId)")
    suspend fun deleteExercisesForRoutine(routineId: Long)

    @Query("SELECT * FROM routine_days WHERE id = :dayId")
    suspend fun getDayById(dayId: Long): RoutineDayEntity?

    @Query("DELETE FROM routines")
    suspend fun deleteAllRoutines()
}