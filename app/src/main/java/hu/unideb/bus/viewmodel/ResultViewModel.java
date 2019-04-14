package hu.unideb.bus.viewmodel;

import android.app.Application;
import android.content.Context;

import org.opentripplanner.api.model.Itinerary;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import hu.unideb.bus.apicall.TripRequest;
import hu.unideb.bus.asynctask.TripPlannerTask;
import hu.unideb.bus.utils.SharedPrefUtils;

public class ResultViewModel extends AndroidViewModel {
    private Context context;
    private static MutableLiveData<List<Itinerary>> itineraries;

    public ResultViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<List<Itinerary>> getItineraries() {
        if (itineraries == null) {
            itineraries = new MutableLiveData<>();
            loadItineraries();
        }

        return itineraries;
    }

    public void invalidateItineraries() {
        itineraries = null;
    }

    private void loadItineraries() {
        TripPlannerTask tripPlannerTask = new TripPlannerTask();
        List<Itinerary> result = tripPlannerTask.getTripPlan(
                new TripRequest(SharedPrefUtils.getFromPlaceLocation(context),
                        SharedPrefUtils.getToPlaceLocation(context),
                        "TRANSIT,WALK",
                        "false"));

        itineraries.setValue(result);
    }
}
