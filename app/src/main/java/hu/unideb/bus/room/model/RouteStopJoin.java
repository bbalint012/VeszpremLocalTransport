package hu.unideb.bus.room.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "route_stop_join",
        primaryKeys = {"routeId", "stopId"},
        foreignKeys = {
                @ForeignKey(entity = RouteEntity.class,
                        parentColumns = "id",
                        childColumns = "routeId"),
                @ForeignKey(entity = StopEntity.class,
                        parentColumns = "id",
                        childColumns = "stopId")
        })
public class RouteStopJoin {
    @NonNull
    private String routeId;

    @ColumnInfo(index = true)
    @NonNull
    private String stopId;

    public RouteStopJoin() {
    }

    @Ignore
    public RouteStopJoin(@NonNull String routeId, @NonNull String stopId) {
        this.routeId = routeId;
        this.stopId = stopId;
    }

    @NonNull
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(@NonNull String routeId) {
        this.routeId = routeId;
    }

    @NonNull
    public String getStopId() {
        return stopId;
    }

    public void setStopId(@NonNull String stopId) {
        this.stopId = stopId;
    }
}
