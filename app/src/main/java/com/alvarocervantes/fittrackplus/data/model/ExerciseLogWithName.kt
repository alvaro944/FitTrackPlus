package com.alvarocervantes.fittrackplus.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class ExerciseLogWithName(
    @Embedded val log: ExerciseLogEntity,
    @ColumnInfo(name = "exercise_name") val exerciseName: String
)
