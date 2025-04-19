package com.alvarocervantes.fittrackplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider
import com.alvarocervantes.fittrackplus.data.model.ExerciseEntity
import com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)
    private val routineDao = db.routineDao()

    fun insertRoutineWithDaysAndExercises(
        routineName: String,
        days: List<Pair<String, List<Triple<String, Int, String>>>> // Día, lista de ejercicios: (nombre, series, repObj)
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val routineId = routineDao.insertRoutine(RoutineEntity(name = routineName))

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
                            series = series
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

}
