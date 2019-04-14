package hu.unideb.bus.room.dao;

import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import hu.unideb.bus.room.model.RouteEntity;

@Dao
public interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RouteEntity entity);

    @Query("SELECT * FROM routes")
    DataSource.Factory<Integer, RouteEntity> getAllRoutes();

    @Query("SELECT * FROM routes WHERE :id = id LIMIT 1")
    RouteEntity getRouteById(String id);

    @Query("SELECT * FROM routes WHERE id IN(:ids)")
    List<RouteEntity> getRoutesByIds(String[] ids);
}
