package hu.unideb.bus.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public final class SharedPrefUtils {
    private static SharedPreferences pref;

    private static SharedPreferences getInstance(Context context) {
        if (pref == null) {
            pref = context.getApplicationContext().getSharedPreferences(SharedPrefKey.PREF_NAME.getKey(), MODE_PRIVATE);
        }
        return pref;
    }

    public static boolean isDbPopulated(Context context) {
        return getInstance(context).getBoolean(SharedPrefKey.IS_DB_POPULATED.getKey(), false);
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = SharedPrefUtils.getInstance(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = SharedPrefUtils.getInstance(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getFromPlaceLocation(Context context) {
        return getInstance(context).getString(SharedPrefKey.FROM_PLACE_LOCATION.getKey(), "");
    }

    public static String getToPlaceLocation(Context context) {
        return getInstance(context).getString(SharedPrefKey.TO_PLACE_LOCATION.getKey(), "");
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.clear();
        editor.apply();
    }
}
