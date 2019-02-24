package hu.unideb.bus.apicall;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.promera.api.responses.Route;
import hu.promera.api.responses.RoutesForAgencyResponse;
import hu.promera.api.service.OneBusAway;
import retrofit2.Call;
import retrofit2.Response;

public class RouteCall {
    private final String TAG = this.getClass().getSimpleName();
    private OneBusAway api;

    public RouteCall() {
        api = ApiClient.getObaClient()
                .create(OneBusAway.class);
    }

    public List<Route> getRoutesForAgency() {
        Call<RoutesForAgencyResponse> call = api.getRoutesForAgency(ApiClient.getAgencyId(), ApiClient.getApiKey());

        try {
            Response<RoutesForAgencyResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getData().getList();
            } else {
                Log.e(TAG, "getRoutesForAgency() DOES NOT success!!" + response.errorBody());
                call.cancel();
            }

        } catch (IOException e) {
            Log.e(TAG, "getRoutesForAgency() failure ", e);
        }
        return new ArrayList<>();
    }
}