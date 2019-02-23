package hu.unideb.bus.room;

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import hu.promera.api.responses.Stop;
import hu.unideb.bus.room.dao.RouteDao;
import hu.unideb.bus.room.dao.RouteStopJoinDao;
import hu.unideb.bus.room.dao.StopDao;
import hu.unideb.bus.room.model.RouteEntity;
import hu.unideb.bus.room.model.RouteStopJoin;
import hu.unideb.bus.room.model.StopEntity;

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

    public void insertRoute(RouteEntity entity) {
        routeDao.insert(entity);
    }

    public void insertStop(StopEntity entity) {
        stopDao.insert(entity);
    }

    public void insertRouteStopJoin(RouteStopJoin entity) {
        routeStopJoinDao.insert(entity);
    }

    public DataSource.Factory<Integer, RouteEntity> getRoutesForPaging() {
        return routeDao.getAllRoutes();
    }

    public LiveData<List<RouteEntity>> getRoutesForStop(String stopId) {
        return routeStopJoinDao.getRoutesForStop(stopId);
    }

    public LiveData<List<StopEntity>> getStops() {
        return stopDao.getAllStops();
    }
}