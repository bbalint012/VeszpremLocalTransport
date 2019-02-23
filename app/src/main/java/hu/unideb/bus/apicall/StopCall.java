package hu.unideb.bus.apicall;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import hu.promera.api.responses.Stop;
import hu.promera.api.responses.StopsForLocationResponse;
import hu.promera.api.responses.StopsForRouteResponse;
import hu.promera.api.service.OneBusAway;
import retrofit2.Call;
import retrofit2.Response;

public class StopCall {
    private final String TAG = this.getClass().getSimpleName();
    private OneBusAway api;

    public StopCall() {
        api = ApiClient.getObaClient()
                .create(OneBusAway.class);
    }

    public List<Stop> getStopsForRoute(String routeId) {
        Call<StopsForRouteResponse> call = api.getStopsForRoute(routeId, ApiClient.getApiKey(), true);

        try {
            Response<StopsForRouteResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                List<Stop> stops = response.body().getData().getReferences().getStops();
                //TODO elkérni a directiont -> ownDirectionba valami felé legyen kiiratáshoz
                return stops;
            } else {
                Log.e(TAG, "getStopsForRoute() DOES NOT success!!" + response.errorBody());
                call.cancel();
            }
        } catch (IOException e) {
            Log.e(TAG, "getStopsForRoute() failure on ID: " + routeId);
        }

        return null;
    }

    public List<Stop> getStopsForLocation(LatLng center) {
        Call<StopsForLocationResponse> call = api.getStopsForLocation(ApiClient.getApiKey(), center.latitude, center.longitude);

        try {
            Response<StopsForLocationResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getData().getList();
            } else {
                Log.e(TAG, "getStopsForLocation DOES NOT success!!" + response.errorBody());
                call.cancel();
            }
        } catch (IOException e) {
            Log.e(TAG, "getStopsForLocation() failure on ID: " + center.toString());
        }

        return null;
    }
}
