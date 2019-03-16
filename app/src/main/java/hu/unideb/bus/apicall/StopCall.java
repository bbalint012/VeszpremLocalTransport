package hu.unideb.bus.apicall;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hu.unideb.api.responses.Stop;
import hu.unideb.api.responses.StopGroup;
import hu.unideb.api.responses.StopsForLocationResponse;
import hu.unideb.api.responses.StopsForRouteResponse;
import hu.unideb.api.service.OneBusAway;
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
                return setStopsForRouteEntities(response);
            } else {
                Log.e(TAG, "getStopsForRoute() DOES NOT success!!" + response.errorBody());
                call.cancel();
            }
        } catch (IOException e) {
            Log.e(TAG, "getStopsForRoute() failure on ID: " + routeId);
        }

        return new ArrayList<>();
    }

    public List<StopEntity> getStopsForLocation(LatLng center) {
        Call<StopsForLocationResponse> call = api.getStopsForLocation(ApiClient.getApiKey(), center.latitude, center.longitude);

        try {
            Response<StopsForLocationResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return setStopsForLocationEntities(response);
            } else {
                Log.e(TAG, "getStopsForLocation DOES NOT success!!" + response.errorBody());
                call.cancel();
            }
        } catch (IOException e) {
            Log.e(TAG, "getStopsForLocation() failure on ID: " + center.toString());
        }

        return new ArrayList<>();
    }


    private List<StopEntity> setStopsForRouteEntities(Response<StopsForRouteResponse> response) {
        List<StopEntity> entities = new LinkedList<>();
        List<Stop> stops = response.body().getData().getReferences().getStops();
        List<StopGroup> stopGroups = response.body().getData().getEntry().getStopGroupings().get(0).getStopGroups();

        for (Stop s : stops) {
            StopEntity entity = new StopEntity(s);
            entity.setName(decodeToUTF8(s.getName()));

            if (stopGroups.get(0).getStopIds().contains(s.getId())) {
                entity.setDestination(decodeToUTF8(stopGroups.get(0).getName().getDestinationName()));
            } else {
                entity.setDestination(decodeToUTF8(stopGroups.get(1).getName().getDestinationName()));
            }
            entities.add(entity);
        }

        return entities;
    }

    private List<StopEntity> setStopsForLocationEntities(Response<StopsForLocationResponse> response) {
        List<StopEntity> entities = new LinkedList<>();
        List<Stop> stops = response.body().getData().getList();

        for (Stop s : stops) {
            StopEntity entity = new StopEntity(s);
            entity.setName(decodeToUTF8(s.getName()));
            entities.add(entity);
        }
        return entities;
    }

    private String decodeToUTF8(String original) {
        return new String(original.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
