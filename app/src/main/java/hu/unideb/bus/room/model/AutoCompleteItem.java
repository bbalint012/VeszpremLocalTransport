package hu.unideb.bus.room.model;

import androidx.annotation.NonNull;

public class AutoCompleteItem {
    private String name;
    private String destination;
    private Double lat;
    private Double lon;

    public AutoCompleteItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getLocation() {
        return String.format("%f,%f", this.lat, this.lon);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s felé", this.name, this.destination);
    }
}
