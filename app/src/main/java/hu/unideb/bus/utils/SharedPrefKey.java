package hu.unideb.bus.utils;

public enum SharedPrefKey {
    PREF_NAME("TrendextPref"),
    IS_DB_POPULATED("isDbPopulated");

    private final String key;

    SharedPrefKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
