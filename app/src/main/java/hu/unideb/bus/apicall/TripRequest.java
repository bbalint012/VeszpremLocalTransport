package hu.unideb.bus.apicall;

import java.util.HashMap;
import java.util.Map;

public class TripRequest {
    private String fromPlace;
    private String toPlace;
    private String mode;
    private String arriveBy;

    public TripRequest(String fromPlace, String toPlace, String mode, String arriveBy) {
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.mode = mode;
        this.arriveBy = arriveBy;
    }

    public Map<String, String> toMap() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("fromPlace", fromPlace);
        queryMap.put("toPlace", toPlace);
        queryMap.put("mode", mode);
        queryMap.put("arriveBy", arriveBy);
        return queryMap;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getArriveBy() {
        return arriveBy;
    }

    public void setArriveBy(String arriveBy) {
        this.arriveBy = arriveBy;
    }
}
