package com.alvarocervantes.fittrackplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider
import com.alvarocervantes.fittrackplus.data.firebase.FirebaseRepository
import com.alvarocervantes.fittrackplus.data.model.ExerciseEntity
import com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)
    private val routineDao = db.routineDao()

    suspend fun getRoutineById(id: Long) = routineDao.getRoutineById(id)

    suspend fun getDaysForRoutine(routineId: Long) = routineDao.getDaysForRoutine(routineId)

    suspend fun getExercisesForDay(dayId: Long) = routineDao.getExercisesForDay(dayId)


    fun insertRoutineWithDaysAndExercises(
        routineName: String,
        days: List<Pair<String, List<Triple<String, Int, String>>>> // Día, lista de ejercicios: (nombre, series, repObj)
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val routineId = routineDao.insertRoutine(RoutineEntity(name = routineName))
            val routine = RoutineEntity(name = routineName, id = routineId)
            FirebaseRepository.uploadRoutine(routine)


            days.forEachIndexed { index, (dayName, exercises) ->
                val dayId = routineDao.insertDay(
                    RoutineDayEntity(
                        routineId = routineId,
                        dayName = dayName.ifBlank { "Sesión ${index + 1}" },
                        dayOrder = index + 1
                    )
                )

                exercises.forEach { (exerciseName, series, repsObj) ->
                    routineDao.insertExercise(
                        ExerciseEntity(
                            dayId = dayId,
                            name = exerciseName,
                            series = series,
                            reps = repsObj
                        )
                    )
                    // repsObj lo podemos guardar después si se expande el modelo
                }
            }
        }
    }
    suspend fun getRoutinesWithDays(): List<Pair<RoutineEntity, Int>> {
        val routines = routineDao.getAllRoutines()
        return routines.map { routine ->
            val days = routineDao.getDaysForRoutine(routine.id)
            routine to days.size
        }
    }

    suspend fun updateRoutineWithNewDaysAndExercises(
        routineId: Long,
        newDays: List<Pair<String, List<Triple<String, Int, String>>>>
    ) {
        val dao = db.routineDao()

        // 1. Eliminar los días y ejercicios antiguos de esa rutina
        dao.deleteExercisesForRoutine(routineId)
        dao.deleteDaysForRoutine(routineId)

        // 2. Insertar los nuevos días y ejercicios
        newDays.forEachIndexed { index, (dayName, exercises) ->
            val dayEntity = RoutineDayEntity(
                routineId = routineId,
                dayName = dayName,
                dayOrder = index
            )
            val dayId = dao.insertDay(dayEntity)

            exercises.forEach { (name, series, reps) ->
                val exercise = ExerciseEntity(
                    dayId = dayId,
                    name = name,
                    series = series,
                    reps = reps
                )
                dao.insertExercise(exercise)
            }
        }
        val updatedRoutine = dao.getRoutineById(routineId)
        updatedRoutine?.let { FirebaseRepository.uploadRoutine(it) }

    }
    suspend fun getAllRoutines(): List<RoutineEntity> {
        return routineDao.getAllRoutines()
    }
    fun deleteRoutine(routine: RoutineEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.routineDao().deleteRoutine(routine)
        }
    }
}
