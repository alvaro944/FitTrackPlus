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

    // Días de rutina
    @Insert suspend fun insertDay(day: RoutineDayEntity): Long
    @Query("SELECT * FROM routine_days WHERE routineId = :routineId ORDER BY dayOrder")
    suspend fun getDaysForRoutine(routineId: Long): List<RoutineDayEntity>

    // Ejercicios definidos
    @Insert suspend fun insertExercise(exercise: ExerciseEntity): Long
    @Query("SELECT * FROM exercises WHERE dayId = :dayId")
    suspend fun getExercisesForDay(dayId: Long): List<ExerciseEntity>

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity?

}
