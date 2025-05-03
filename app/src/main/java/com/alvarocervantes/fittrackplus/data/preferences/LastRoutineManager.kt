package com.alvarocervantes.fittrackplus.data.preferences

import android.content.Context
import android.content.SharedPreferences

object LastRoutineManager {

    private const val PREFS_NAME = "last_routine_prefs"
    private const val KEY_LAST_ROUTINE_ID = "last_routine_id"

    fun saveLastRoutineId(context: Context, routineId: Long) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putLong(KEY_LAST_ROUTINE_ID, routineId)
            .apply()
    }

    fun getLastRoutineId(context: Context): Long {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(KEY_LAST_ROUTINE_ID, -1L)
    }

    fun clearLastRoutineId(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_LAST_ROUTINE_ID)
            .apply()
    }
}

