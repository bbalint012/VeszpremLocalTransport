package hu.unideb.bus.room.model;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import hu.unideb.api.responses.Route;

@Entity(tableName = "routes")
public class RouteEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String agencyId;
    private String description;
    private String longName;
    private String shortName;

    public RouteEntity() {
    }

    @Ignore
    public RouteEntity(@NonNull Route route) {
        this.id = route.getId();
        this.agencyId = route.getAgencyId();
        this.description = route.getDescription();
        this.longName = route.getLongName();
        this.shortName = route.getShortName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RouteEntity that = (RouteEntity) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.shortName, that.shortName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortName);
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
