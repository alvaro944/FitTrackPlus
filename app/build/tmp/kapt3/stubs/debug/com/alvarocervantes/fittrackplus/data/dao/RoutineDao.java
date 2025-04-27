package com.alvarocervantes.fittrackplus.data.dao;

import androidx.room.*;
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity;
import com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity;
import com.alvarocervantes.fittrackplus.data.model.ExerciseEntity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u001b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\b2\u0006\u0010\u0011\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\b2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001b\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0015\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018J\u0019\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u0019\u0010\u001c\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001d"}, d2 = {"Lcom/alvarocervantes/fittrackplus/data/dao/RoutineDao;", "", "deleteRoutine", "", "routine", "Lcom/alvarocervantes/fittrackplus/data/model/RoutineEntity;", "(Lcom/alvarocervantes/fittrackplus/data/model/RoutineEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllRoutines", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDayNameById", "", "dayId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDaysForRoutine", "Lcom/alvarocervantes/fittrackplus/data/model/RoutineDayEntity;", "routineId", "getExercisesForDay", "Lcom/alvarocervantes/fittrackplus/data/model/ExerciseEntity;", "getRoutineById", "id", "insertDay", "day", "(Lcom/alvarocervantes/fittrackplus/data/model/RoutineDayEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertExercise", "exercise", "(Lcom/alvarocervantes/fittrackplus/data/model/ExerciseEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertRoutine", "app_debug"})
@androidx.room.Dao
public abstract interface RoutineDao {
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertRoutine(@org.jetbrains.annotations.NotNull
    com.alvarocervantes.fittrackplus.data.model.RoutineEntity routine, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM routines")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getAllRoutines(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.RoutineEntity>> $completion);
    
    @androidx.room.Delete
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteRoutine(@org.jetbrains.annotations.NotNull
    com.alvarocervantes.fittrackplus.data.model.RoutineEntity routine, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertDay(@org.jetbrains.annotations.NotNull
    com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity day, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM routine_days WHERE routineId = :routineId ORDER BY dayOrder")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getDaysForRoutine(long routineId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity>> $completion);
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertExercise(@org.jetbrains.annotations.NotNull
    com.alvarocervantes.fittrackplus.data.model.ExerciseEntity exercise, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM exercises WHERE dayId = :dayId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getExercisesForDay(long dayId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.ExerciseEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM routines WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getRoutineById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.alvarocervantes.fittrackplus.data.model.RoutineEntity> $completion);
    
    @androidx.room.Query(value = "SELECT dayName FROM routine_days WHERE id = :dayId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getDayNameById(long dayId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.String> $completion);
}