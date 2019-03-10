package hu.unideb.bus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.opentripplanner.api.model.Leg;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public final class SharedPrefUtils {
    private static SharedPreferences pref;

    public static SharedPreferences getInstance(Context context) {
        if (pref == null) {
            pref = context.getApplicationContext().getSharedPreferences(SharedPrefKey.PREF_NAME.getKey(), MODE_PRIVATE);
        }
        return pref;
    }

    public static void saveItinerary(Context context, ArrayList<Leg> legs) {
        Gson gson = new Gson();

        ArrayList<String> legJson = new ArrayList<>();
        for (Leg leg : legs) {
            legJson.add(gson.toJson(leg));
        }
        putListString(context, SharedPrefKey.LEGS.getKey(), legJson);
    }

    public static ArrayList<Leg> getItinerary(Context context) {
        Gson gson = new Gson();

        ArrayList<String> legJson = getListString(context, SharedPrefKey.LEGS.getKey());
        ArrayList<Leg> itineraries = new ArrayList<>();

        for (String leg : legJson) {
            Leg result = gson.fromJson(leg, Leg.class);
            itineraries.add(result);
        }
        return itineraries;
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

    public static LatLng getDefaultLatLng(Context context) {
        final String latitude = getInstance(context).getString(SharedPrefKey.DEFAULT_LATITUDE.getKey(), "0");
        final String longitude = getInstance(context).getString(SharedPrefKey.DEFAULT_LONGITUDE.getKey(), "0");
        return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    private static void putListString(Context context, String key, ArrayList<String> stringList) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        saveString(context, key, TextUtils.join("‚‗‚", myStringList));
    }

    private static ArrayList<String> getListString(Context context, String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(getInstance(context).getString(key, ""), "‚‗‚")));
    }

    public static void remove(Context context, SharedPrefKey key) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.remove(key.getKey());
        editor.apply();
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.clear();
        editor.apply();
    }
}
