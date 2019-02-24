package hu.unideb.bus.room;

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import hu.unideb.bus.room.dao.RouteDao;
import hu.unideb.bus.room.dao.RouteStopJoinDao;
import hu.unideb.bus.room.dao.StopDao;
import hu.unideb.bus.room.model.RouteEntity;
import hu.unideb.bus.room.model.RouteStopJoin;
import hu.unideb.bus.room.model.StopEntity;
import hu.unideb.bus.room.model.StopWithDestination;

public class BusRepository {
    private static BusRepository INSTANCE;
    private RouteDao routeDao;
    private StopDao stopDao;
    private RouteStopJoinDao routeStopJoinDao;

    private BusRepository(Context context) {
        BusDatabase db = BusDatabase.getInstance(context);
        routeDao = db.getRouteDao();
        stopDao = db.getStopDao();
        routeStopJoinDao = db.getRouteStopJoinDao();
    }

    public static synchronized BusRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BusRepository(context);
        }
        return INSTANCE;
    }

    //Route
    public void insertRoute(RouteEntity entity) {
        routeDao.insert(entity);
    }

    public DataSource.Factory<Integer, RouteEntity> getRoutesForPaging() {
        return routeDao.getAllRoutes();
    }

    //RouteStopJoin
    public void insertRouteStopJoin(RouteStopJoin entity) {
        routeStopJoinDao.insert(entity);
    }

    public LiveData<List<RouteEntity>> getRoutesForStop(String stopId) {
        return routeStopJoinDao.getRoutesForStop(stopId);
    }

    //Stop
    public void insertStop(StopEntity entity) {
        stopDao.insert(entity);
    }

    public LiveData<List<StopEntity>> getStops() {
        return stopDao.getAllStops();
    }

    public LiveData<List<StopWithDestination>> getStopsWithDestinations() {
        return stopDao.getStopsWithDestinations();
    }

}