package com.alvarocervantes.fittrackplus.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.alvarocervantes.fittrackplus.data.dao.RoutineDao;
import com.alvarocervantes.fittrackplus.data.dao.SessionDao;
import com.alvarocervantes.fittrackplus.data.model.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/alvarocervantes/fittrackplus/data/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "routineDao", "Lcom/alvarocervantes/fittrackplus/data/dao/RoutineDao;", "sessionDao", "Lcom/alvarocervantes/fittrackplus/data/dao/SessionDao;", "app_debug"})
@androidx.room.Database(entities = {com.alvarocervantes.fittrackplus.data.model.RoutineEntity.class, com.alvarocervantes.fittrackplus.data.model.RoutineDayEntity.class, com.alvarocervantes.fittrackplus.data.model.ExerciseEntity.class, com.alvarocervantes.fittrackplus.data.model.SessionEntity.class, com.alvarocervantes.fittrackplus.data.model.ExerciseLogEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract com.alvarocervantes.fittrackplus.data.dao.RoutineDao routineDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.alvarocervantes.fittrackplus.data.dao.SessionDao sessionDao();
}