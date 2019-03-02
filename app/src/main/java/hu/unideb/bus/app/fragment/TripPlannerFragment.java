package hu.unideb.bus.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.model.Leg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import hu.unideb.bus.R;
import hu.unideb.bus.apicall.TripRequest;
import hu.unideb.bus.asynctask.TripPlannerTask;
import hu.unideb.bus.room.BusRepository;
import hu.unideb.bus.room.model.AutoCompleteItem;
import hu.unideb.bus.ui.PolylineDrawer;
import hu.unideb.bus.ui.CustomAdapter;
import hu.unideb.bus.utils.LocationUtil;
import hu.unideb.bus.utils.Utils;

public class TripPlannerFragment extends Fragment implements OnMapReadyCallback {
    private final String TAG = this.getClass().getSimpleName();

    private AutoCompleteTextView fromPlace;
    private AutoCompleteTextView toPlace;
    private String fromPlaceLocation;
    private String toPlaceLocation;
    private MapView mMapView;
    private GoogleMap mMap;
    private PolylineDrawer polylineDrawer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip_planner, container, false);
        fromPlace = (AutoCompleteTextView) view.findViewById(R.id.fromPlace);
        toPlace = (AutoCompleteTextView) view.findViewById(R.id.toPlace);
        mMapView = (MapView) view.findViewById(R.id.tripPlannerMapView);

        Utils.setToolbar((AppCompatActivity) getActivity(), this, view, R.id.tripPlannerToolbar);
        setSearchBtn(view);
        setupEditTextListeners();

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
        polylineDrawer = new PolylineDrawer(getActivity(), mMap);
        setMapControls();
    }

    private void setMapControls() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void showRouteOnMap() {
        final List<Leg> itinerary = getItineraries();
        if (itinerary.isEmpty()) {
            noRouteFound();
            return;
        }

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (Leg leg : itinerary) {
            List<LatLng> points = LocationUtil.decodePoly(leg.legGeometry.getPoints());
            if (points.isEmpty()) {
                return;
            }

            polylineDrawer.addMarkers(points);
            polylineDrawer.draw(leg, points);

            for (LatLng point : points) {
                builder.include(point);
            }
        }

        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 48));
        }
    }

    private List<Leg> getItineraries() {
        if (fromPlaceLocation == null || fromPlaceLocation.isEmpty() ||
                toPlaceLocation == null || toPlaceLocation.isEmpty()) {
            Utils.showToast(getActivity(), getResources().getString(R.string.plsWriteSmth), Toast.LENGTH_LONG);
            return new ArrayList<>();
        }
        System.out.println(fromPlaceLocation);
        TripPlannerTask tripPlannerTask = new TripPlannerTask();
        List<Itinerary> result = tripPlannerTask.getTripPlan(
                new TripRequest(fromPlaceLocation, toPlaceLocation, getCurrentTime(),
                        getCurrentDate(), "TRANSIT,WALK", "false"));

        if (result.isEmpty()) {
            return new ArrayList<>();
        }

        
        return result.get(0).legs;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", new Locale("hu"));
        return sdf.format(System.currentTimeMillis());
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", new Locale("hu"));
        return sdf.format(System.currentTimeMillis());
    }

    private void setAutoCompleteTextViews() {
        BusRepository.getInstance(getActivity())
                .getStopsWithDestinations().observe(this, items -> {
            CustomAdapter adapter = new CustomAdapter(getActivity(), items);
            fromPlace.setAdapter(adapter);
            fromPlace.setOnItemClickListener((parent, v, position, id) -> {
                fromPlaceLocation = adapter.getItem(position).getLocation();
            });

            CustomAdapter adapter2 = new CustomAdapter(getActivity(), items);
            toPlace.setAdapter(adapter2);
            toPlace.setOnItemClickListener((parent, v, position, id) -> {
                toPlaceLocation = adapter2.getItem(position).getLocation();
            });
        });
    }

    private void setSearchBtn(View view) {
        ImageButton btnAdd = (ImageButton) view.findViewById(R.id.btnSearch);
        btnAdd.setOnClickListener(v ->
                showRouteOnMap()
        );
    }

    private void setupEditTextListeners() {
        toPlace.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Utils.hideKeyboard(v, getActivity());
                showRouteOnMap();
                handled = true;
            }
            return handled;
        });

        fromPlace.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Utils.hideKeyboard(v, getActivity());
            }
        });

        toPlace.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Utils.hideKeyboard(v, getActivity());
            }
        });
    }

    private void noRouteFound() {
        fromPlace.setText("");
        toPlace.setText("");
        Utils.showToast(getActivity(), getResources().getString(R.string.sryRouteNotFound), Toast.LENGTH_LONG);
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
