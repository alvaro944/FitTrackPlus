package com.alvarocervantes.fittrackplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider
import com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity
import com.alvarocervantes.fittrackplus.data.model.ExerciseLogWithName
import com.alvarocervantes.fittrackplus.data.model.SessionEntity

class TrainingHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)
    private val sessionDao = db.sessionDao()
    private var routineId: Long? = null

    /**
     * Devuelve una lista de Pair de:
     * - Sesión de entrenamiento
     * - Lista de ejercicios realizados
     */
    suspend fun getTrainingHistory(): List<Pair<SessionEntity, List<ExerciseLogWithName>>> {
        val sessions = if (routineId != null) {
            sessionDao.getSessionsForRoutine(routineId!!)
        } else {
            sessionDao.getAllSessions()
        }

        return sessions.map { session ->
            val exercises = sessionDao.getLogsWithNamesForSession(session.id)
            session to exercises
        }
    }


    suspend fun getDayName(dayId: Long): String? {
        return db.routineDao().getDayNameById(dayId)
    }

    fun setRoutineId(id: Long){
        routineId = id
    }
    suspend fun getRoutineNameForCurrentSession(): String {
        val id = routineId ?: return "Rutina actual"
        val routine = db.routineDao().getRoutineById(id)
        return routine?.name ?: "Rutina actual"
    }

}

