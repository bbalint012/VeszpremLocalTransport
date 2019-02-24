package hu.unideb.bus.apicall;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hu.promera.api.responses.Stop;
import hu.promera.api.responses.StopGroup;
import hu.promera.api.responses.StopsForLocationResponse;
import hu.promera.api.responses.StopsForRouteResponse;
import hu.promera.api.service.OneBusAway;
import hu.unideb.bus.room.model.StopEntity;
import retrofit2.Call;
import retrofit2.Response;

public class StopCall {
    private final String TAG = this.getClass().getSimpleName();
    private OneBusAway api;

    public StopCall() {
        api = ApiClient.getObaClient()
                .create(OneBusAway.class);
    }

    public List<StopEntity> getStopsForRoute(String routeId) {
        Call<StopsForRouteResponse> call = api.getStopsForRoute(routeId, ApiClient.getApiKey(), true);

        try {
            Response<StopsForRouteResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return setStopEntities(response);
            } else {
                Log.e(TAG, "getStopsForRoute() DOES NOT success!!" + response.errorBody());
                call.cancel();
            }
        } catch (IOException e) {
            Log.e(TAG, "getStopsForRoute() failure on ID: " + routeId);
        }

        return new ArrayList<>();
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

        return new ArrayList<>();
    }


    private List<StopEntity> setStopEntities(Response<StopsForRouteResponse> response) {
        List<StopEntity> entities = new LinkedList<>();
        List<Stop> stops = response.body().getData().getReferences().getStops();
        List<StopGroup> stopGroups = response.body().getData().getEntry().getStopGroupings().get(0).getStopGroups();

        for (Stop s : stops) {
            StopEntity entity = new StopEntity(s);
            if (stopGroups.get(0).getStopIds().contains(s.getId())) {
                entity.setDestination(stopGroups.get(0).getName().getDestinationName());
            } else {
                entity.setDestination(stopGroups.get(1).getName().getDestinationName());
            }
            entities.add(entity);
        }

        return entities;
    }
}
