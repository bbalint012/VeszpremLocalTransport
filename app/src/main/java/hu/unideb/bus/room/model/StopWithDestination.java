package hu.unideb.bus.room.model;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

public class StopWithDestination {
    private String name;
    private String destination;

    public StopWithDestination() {
    }

    @Ignore
    public StopWithDestination(String name, String destination) {
        this.name = name;
        this.destination = destination;
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

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s felé", this.name, this.destination);
    }
}
