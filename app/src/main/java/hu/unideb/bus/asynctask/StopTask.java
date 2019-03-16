package hu.unideb.bus.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hu.unideb.bus.apicall.StopCall;
import hu.unideb.bus.room.model.StopEntity;

public class StopTask {
    private final String TAG = this.getClass().getSimpleName();

    public List<StopEntity> getStopsForLocation(LatLng center) {
        try {
            return new StopsForLocationAsyncTask(center).execute().get();
        } catch (Exception e) {
            Log.e(TAG, "StopsForLocationAsyncTask failed ", e);
            return new ArrayList<>();
        }
    }

    private static class StopsForLocationAsyncTask extends AsyncTask<Void, Void, List<StopEntity>> {
        private StopCall call;
        private LatLng center;

        StopsForLocationAsyncTask(LatLng center) {
            this.call = new StopCall();
            this.center = center;
        }

        @Override
        protected List<StopEntity> doInBackground(final Void... params) {
            return call.getStopsForLocation(center);
        }
    }
}
