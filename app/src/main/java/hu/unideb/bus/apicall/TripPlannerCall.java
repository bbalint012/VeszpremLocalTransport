package hu.unideb.bus.apicall;

import android.util.Log;

import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.ws.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.unideb.otp.service.OpenTripPlanner;
import retrofit2.Call;

public class TripPlannerCall {
    private final String TAG = this.getClass().getSimpleName();
    private OpenTripPlanner api;

    public TripPlannerCall() {
        api = ApiClient.getOtpClient()
                .create(OpenTripPlanner.class);
    }

    public List<Itinerary> getTripPlan(TripRequest request) {
        Map<String, String> queryMap = request.toMap();
        Call<Response> call = api.getTripPlan(queryMap);

        try {
            retrofit2.Response<Response> response = call.execute();

            if (response.isSuccessful() &&
                    response.body() != null &&
                    response.body().getError() == null) {
                return response.body().getPlan().getItinerary();
            } else {
                call.cancel();
                Log.e(TAG, "getTripPlan() DOES NOT success!!" + response.errorBody());
                if (response.body().getError() != null) {
                    Log.e(TAG, response.body().getError().getMsg());
                }
            }

        } catch (IOException e) {
            Log.e(TAG, "getTripPlan() failure ", e);
        }
        return new ArrayList<>();
    }
}
