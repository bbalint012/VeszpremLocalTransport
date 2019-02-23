package hu.unideb.bus.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import hu.unideb.bus.room.dao.RouteDao;
import hu.unideb.bus.room.dao.RouteStopJoinDao;
import hu.unideb.bus.room.dao.StopDao;
import hu.unideb.bus.room.model.RouteEntity;
import hu.unideb.bus.room.model.RouteStopJoin;
import hu.unideb.bus.room.model.StopEntity;

@Database(entities = {RouteEntity.class, StopEntity.class, RouteStopJoin.class}, version = 1, exportSchema = false)
public abstract class BusDatabase extends RoomDatabase {
    private static final String DB_NAME = "routes-db";
    private static volatile BusDatabase INSTANCE;

    static synchronized BusDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BusDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, BusDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract RouteDao getRouteDao();

    public abstract StopDao getStopDao();

    public abstract RouteStopJoinDao getRouteStopJoinDao();
}