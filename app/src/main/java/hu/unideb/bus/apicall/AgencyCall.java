package hu.unideb.bus.apicall;

import android.location.Location;
import android.util.Log;

import java.io.IOException;

import hu.unideb.api.responses.AgenciesWithCoverageResponse;
import hu.unideb.api.service.OneBusAway;
import retrofit2.Call;
import retrofit2.Response;

public class AgencyCall {
    private final String TAG = this.getClass().getSimpleName();
    private OneBusAway api;

    public AgencyCall() {
        api = ApiClient.getObaClient()
                .create(OneBusAway.class);
    }

    public Location getAgencyCoverageArea() {
        final Location defaultLocation = new Location("defaultLocation");
        final Call<AgenciesWithCoverageResponse> call = api.getAgencies(ApiClient.getApiKey());

        try {
            final Response<AgenciesWithCoverageResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                defaultLocation.setLatitude(response.body().getData().getList().get(0).getLat());
                defaultLocation.setLongitude(response.body().getData().getList().get(0).getLon());
                return defaultLocation;
            } else {
                Log.e(TAG, "getAgencyCoverageArea() DOES NOT success!!" + response.errorBody());
                call.cancel();
            }

        } catch (IOException e) {
            Log.e(TAG, "getAgencyCoverageArea() failure ", e);
        }
        return defaultLocation;
    }
}
