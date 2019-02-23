package hu.unideb.bus.apicall;

import android.util.Log;

import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.ws.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import hu.promera.otp.service.OpenTripPlanner;
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

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getPlan().getItinerary();
            } else {
                Log.e(TAG, "getTripPlan() DOES NOT success!!" + response.errorBody());
                call.cancel();
            }

        } catch (IOException e) {
            Log.e(TAG, "getTripPlan() failure ", e);
        }
        return null;
    }
}
