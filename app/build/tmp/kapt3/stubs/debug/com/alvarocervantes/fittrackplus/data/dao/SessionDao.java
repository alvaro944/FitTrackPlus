package com.alvarocervantes.fittrackplus.data.dao;

import androidx.room.*;
import com.alvarocervantes.fittrackplus.data.model.SessionEntity;
import com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\f\bg\u0018\u00002\u00020\u0001J\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0005J\u001f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u001f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00032\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u001f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u000e\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0019\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0014\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0015"}, d2 = {"Lcom/alvarocervantes/fittrackplus/data/dao/SessionDao;", "", "getAllSessions", "", "Lcom/alvarocervantes/fittrackplus/data/model/SessionEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLogsByExerciseOrdered", "Lcom/alvarocervantes/fittrackplus/data/model/ExerciseLogEntity;", "exerciseId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLogsForSession", "sessionId", "getSessionsByRoutine", "routineId", "insertExerciseLog", "log", "(Lcom/alvarocervantes/fittrackplus/data/model/ExerciseLogEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSession", "session", "(Lcom/alvarocervantes/fittrackplus/data/model/SessionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface SessionDao {
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertSession(@org.jetbrains.annotations.NotNull
    com.alvarocervantes.fittrackplus.data.model.SessionEntity session, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions ORDER BY date DESC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getAllSessions(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.SessionEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sessions WHERE routineId = :routineId ORDER BY date DESC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getSessionsByRoutine(long routineId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.SessionEntity>> $completion);
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertExerciseLog(@org.jetbrains.annotations.NotNull
    com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity log, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM exercise_logs WHERE sessionId = :sessionId ORDER BY exerciseId, seriesNumber")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getLogsForSession(long sessionId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity>> $completion);
    
    @androidx.room.Query(value = "\n    SELECT el.* FROM exercise_logs el\n    INNER JOIN sessions s ON el.sessionId = s.id\n    WHERE el.exerciseId = :exerciseId\n    ORDER BY s.date DESC\n")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getLogsByExerciseOrdered(long exerciseId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity>> $completion);
}