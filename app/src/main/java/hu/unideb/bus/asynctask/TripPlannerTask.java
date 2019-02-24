package hu.unideb.bus.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import org.opentripplanner.api.model.Itinerary;

import java.util.ArrayList;
import java.util.List;

import hu.unideb.bus.apicall.TripPlannerCall;
import hu.unideb.bus.apicall.TripRequest;

public class TripPlannerTask {
    private final String TAG = this.getClass().getSimpleName();

    public List<Itinerary> getTripPlan(TripRequest request) {
        try {
            return new TripPlannerAsyncTask(request).execute().get();
        } catch (Exception e) {
            Log.e(TAG, "TripPlannerAsyncTask failed ", e);
            return new ArrayList<>();
        }
    }

    private static class TripPlannerAsyncTask extends AsyncTask<Void, Void, List<Itinerary>> {
        private TripPlannerCall call;
        private TripRequest request;

        TripPlannerAsyncTask(TripRequest request) {
            this.call = new TripPlannerCall();
            this.request = request;
        }

        @Override
        protected List<Itinerary> doInBackground(final Void... params) {
            return call.getTripPlan(request);
        }
    }
}
