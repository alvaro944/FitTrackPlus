package com.alvarocervantes.fittrackplus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alvarocervantes.fittrackplus.data.dao.RoutineDao
import com.alvarocervantes.fittrackplus.data.dao.SessionDao
import com.alvarocervantes.fittrackplus.data.model.*

@Database(
    entities = [
        RoutineEntity::class,
        RoutineDayEntity::class,
        ExerciseEntity::class,
        SessionEntity::class,
        ExerciseLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun sessionDao(): SessionDao
}

