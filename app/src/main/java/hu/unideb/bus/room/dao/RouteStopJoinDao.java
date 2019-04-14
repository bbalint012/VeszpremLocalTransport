package hu.unideb.bus.room.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import hu.unideb.bus.room.model.RouteEntity;
import hu.unideb.bus.room.model.RouteStopJoin;
import hu.unideb.bus.room.model.StopEntity;

@Dao
public interface RouteStopJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RouteStopJoin userRepoJoin);

    @Query("SELECT * FROM routes " +
            "INNER JOIN route_stop_join " +
            "ON routes.id = route_stop_join.routeId " +
            "WHERE route_stop_join.stopId = :stopId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    LiveData<List<RouteEntity>> getRoutesForStop(String stopId);

    @Query("SELECT * FROM stops " +
            "INNER JOIN route_stop_join " +
            "ON stops.id = route_stop_join.stopId " +
            "WHERE route_stop_join.routeId = :routeId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    LiveData<List<StopEntity>> getStopsForRoutes(String routeId);
}
