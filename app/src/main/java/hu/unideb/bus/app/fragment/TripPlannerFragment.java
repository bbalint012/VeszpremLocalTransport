package hu.unideb.bus.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;
import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.model.Leg;
import org.opentripplanner.routing.core.TraverseMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import hu.unideb.bus.R;
import hu.unideb.bus.apicall.TripRequest;
import hu.unideb.bus.asynctask.TripPlannerTask;
import hu.unideb.bus.room.BusRepository;
import hu.unideb.bus.room.model.StopWithDestination;
import hu.unideb.bus.utils.LocationUtil;
import hu.unideb.bus.utils.Utils;

public class TripPlannerFragment extends Fragment implements OnMapReadyCallback {
    private final String TAG = this.getClass().getSimpleName();

    AutoCompleteTextView fromPlace;
    AutoCompleteTextView toPlace;
    private MapView mMapView;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip_planner, container, false);
        Utils.setToolbar((AppCompatActivity) getActivity(), this, view, R.id.tripPlannerToolbar);

        fromPlace = (AutoCompleteTextView) view.findViewById(R.id.fromPlace);
        toPlace = (AutoCompleteTextView) view.findViewById(R.id.toPlace);
        mMapView = (MapView) view.findViewById(R.id.tripPlannerMapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "Map initializing failed ", e);
        }
        mMapView.getMapAsync(this);

        setAutoCompleteTextViews();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapControls();
        showRouteOnMap(getItineraries());
    }

    private void setMapControls() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void showRouteOnMap(@NotNull List<Leg> itinerary) {
        if (itinerary.isEmpty()) {
            return;
        }
        Marker firstTransitMarker = null;

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (Leg leg : itinerary) {
            List<LatLng> points = LocationUtil.decodePoly(leg.legGeometry.getPoints());

            if (firstTransitMarker == null && TraverseMode.valueOf(leg.mode).isTransit()) {
                firstTransitMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(points.get(0).latitude, points.get(0).longitude))
                        .title("first")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_marker)));
            }

            if (!points.isEmpty()) {
                float scaleFactor = getResources().getFraction(R.fraction.scaleFactor, 1, 1);
                PolylineOptions options = new PolylineOptions()
                        .addAll(points)
                        .width(5 * scaleFactor)
                        .color(getResources().getColor(R.color.blue));
                mMap.addPolyline(options);

                for (LatLng point : points) {
                    builder.include(point);
                }
            }
        }

        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 48));
        }

    }

    private List<Leg> getItineraries() {
        //TODO:

        TripPlannerTask tripPlannerTask = new TripPlannerTask();
        List<Itinerary> result = tripPlannerTask.getTripPlan(
                new TripRequest("47.52253,19.07229", "47.51473,19.04157", "1:02pm",
                        "02-06-2019", "TRANSIT,WALK", "500", "false"));
        return result.get(0).legs;
    }

    private void setAutoCompleteTextViews() {
        BusRepository.getInstance(getActivity())
                .getStopsWithDestinations().observe(this, list -> {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getActivity(), android.R.layout.two_line_list_item, android.R.id.text2, convertToString(list));
            fromPlace.setAdapter(adapter);
            toPlace.setAdapter(adapter);
        });
    }

    private List<String> convertToString(List<StopWithDestination> input) {
        List<String> result = new ArrayList<>();
        for (StopWithDestination s : input) {
            result.add(s.toString());
        }
        return result;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }
}
