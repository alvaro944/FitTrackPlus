package com.alvarocervantes.fittrackplus.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider
import com.alvarocervantes.fittrackplus.data.model.*
import com.alvarocervantes.fittrackplus.ui.fragments.training.RegisterTrainingFragment

class RegisterTrainingViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)
    private val routineDao = db.routineDao()
    private val sessionDao = db.sessionDao()

    suspend fun getNextRoutineDay(routineId: Long): RoutineDayEntity? {
        val days = routineDao.getDaysForRoutine(routineId)
        if (days.isEmpty()) return null

        val sessions = sessionDao.getSessionsByRoutine(routineId)

        // ⚠️ Cuenta solo las sesiones con dayId que esté dentro de los días actuales
        val validSessions = sessions.filter { it.dayId in days.map { d -> d.id } }

        // ✅ Cálculo correcto del índice del próximo día
        val nextDayIndex = validSessions.size % days.size
        val weekNumber = validSessions.size / days.size + 1

        val nextDay = days[nextDayIndex]

        Log.d("Registro", "🧠 Sesiones de rutina actual: ${validSessions.size}")
        Log.d("Registro", "📆 Semana actual: $weekNumber")
        Log.d("Registro", "➡️ Día siguiente: ${nextDay.dayName}")

        return nextDay
    }


    suspend fun getExercisesForDay(dayId: Long): List<ExerciseEntity> {
        return routineDao.getExercisesForDay(dayId)
    }

    suspend fun insertSessionWithWeek(routineId: Long, dayId: Long): Long {
        val allDays = routineDao.getDaysForRoutine(routineId)
        val sessions = sessionDao.getSessionsByRoutine(routineId)
        val validSessions = sessions.filter { it.dayId in allDays.map { it.id } }

        val weekNumber = validSessions.size / allDays.size + 1

        val session = SessionEntity(
            routineId = routineId,
            dayId = dayId,
            date = getCurrentDateIso(), // Ahora sí compila
            week = weekNumber,
            comment = null
        )

        return sessionDao.insertSession(session)
    }



    suspend fun insertLog(log: ExerciseLogEntity): Long {
        return sessionDao.insertExerciseLog(log)
    }
    suspend fun getRoutineById(id: Long): RoutineEntity? {
        return routineDao.getRoutineById(id)
    }

    private fun getCurrentDateIso(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val date = java.util.Date(currentTimeMillis)
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return format.format(date)
    }
    suspend fun getSessionsForRoutine(routineId: Long): List<SessionEntity> {
        return sessionDao.getSessionsByRoutine(routineId)
    }



}
