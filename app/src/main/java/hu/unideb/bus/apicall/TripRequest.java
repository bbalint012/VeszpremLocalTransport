package hu.unideb.bus.apicall;

import java.util.HashMap;
import java.util.Map;

public class TripRequest {
    private String fromPlace;
    private String toPlace;
    private String time;
    private String date;
    private String mode;
    private String arriveBy;

    public TripRequest() {
    }

    public TripRequest(String fromPlace, String toPlace, String time, String date, String mode, String arriveBy) {
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.time = time;
        this.date = date;
        this.mode = mode;
        this.arriveBy = arriveBy;
    }

    public Map<String, String> toMap() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("fromPlace", fromPlace);
        queryMap.put("toPlace", toPlace);
        queryMap.put("time", time);
        queryMap.put("date", date);
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
