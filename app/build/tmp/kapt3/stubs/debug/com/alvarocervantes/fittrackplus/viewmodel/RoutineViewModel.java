package com.alvarocervantes.fittrackplus.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;
import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider;
import com.alvarocervantes.fittrackplus.data.model.ExerciseEntity;
import com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity;
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity;
import kotlinx.coroutines.Dispatchers;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\n2\u0006\u0010\u0011\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ#\u0010\u0015\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00170\u00160\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018J@\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c20\u0010\u001d\u001a,\u0012(\u0012&\u0012\u0004\u0012\u00020\u001c\u0012\u001c\u0012\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u001c0\u001e0\n0\u00160\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001f"}, d2 = {"Lcom/alvarocervantes/fittrackplus/viewmodel/RoutineViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "db", "Lcom/alvarocervantes/fittrackplus/data/database/AppDatabase;", "routineDao", "Lcom/alvarocervantes/fittrackplus/data/dao/RoutineDao;", "getDaysForRoutine", "", "Lcom/alvarocervantes/fittrackplus/data/model/RoutineDayEntity;", "routineId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getExercisesForDay", "Lcom/alvarocervantes/fittrackplus/data/model/ExerciseEntity;", "dayId", "getRoutineById", "Lcom/alvarocervantes/fittrackplus/data/model/RoutineEntity;", "id", "getRoutinesWithDays", "Lkotlin/Pair;", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertRoutineWithDaysAndExercises", "", "routineName", "", "days", "Lkotlin/Triple;", "app_debug"})
public final class RoutineViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.alvarocervantes.fittrackplus.data.database.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull
    private final com.alvarocervantes.fittrackplus.data.dao.RoutineDao routineDao = null;
    
    public RoutineViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getRoutineById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.alvarocervantes.fittrackplus.data.model.RoutineEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getDaysForRoutine(long routineId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getExercisesForDay(long dayId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.alvarocervantes.fittrackplus.data.model.ExerciseEntity>> $completion) {
        return null;
    }
    
    public final void insertRoutineWithDaysAndExercises(@org.jetbrains.annotations.NotNull
    java.lang.String routineName, @org.jetbrains.annotations.NotNull
    java.util.List<? extends kotlin.Pair<java.lang.String, ? extends java.util.List<kotlin.Triple<java.lang.String, java.lang.Integer, java.lang.String>>>> days) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getRoutinesWithDays(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<kotlin.Pair<com.alvarocervantes.fittrackplus.data.model.RoutineEntity, java.lang.Integer>>> $completion) {
        return null;
    }
}