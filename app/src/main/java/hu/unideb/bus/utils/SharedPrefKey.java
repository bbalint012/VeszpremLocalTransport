package hu.unideb.bus.utils;

public enum SharedPrefKey {
    PREF_NAME("TrendextPref"),
    IS_DB_POPULATED("isDbPopulated"),
    FROM_PLACE_LOCATION("fromPlaceLocation"),
    TO_PLACE_LOCATION("toPlaceLocation"),
    DEFAULT_LATITUDE("defaultLatitude"),
    DEFAULT_LONGITUDE("defaultLongitude"),
    LEGS("legs");

    private final String key;

    SharedPrefKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
