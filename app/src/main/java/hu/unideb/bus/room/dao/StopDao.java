package hu.unideb.bus.room.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import hu.unideb.bus.room.model.StopEntity;
import hu.unideb.bus.room.model.StopWithDestination;

@Dao
public interface StopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StopEntity stop);

    @Query("SELECT * FROM stops")
    LiveData<List<StopEntity>> getAllStops();

    @Query("SELECT s.name, s.destination FROM stops s")
    LiveData<List<StopWithDestination>> getStopsWithDestinations();
}
