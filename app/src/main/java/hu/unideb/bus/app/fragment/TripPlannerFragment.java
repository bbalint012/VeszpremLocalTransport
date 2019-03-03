package hu.unideb.bus.app.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.opentripplanner.api.model.Itinerary;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import hu.unideb.bus.R;
import hu.unideb.bus.apicall.TripRequest;
import hu.unideb.bus.app.RouteActivity;
import hu.unideb.bus.asynctask.TripPlannerTask;
import hu.unideb.bus.room.BusRepository;
import hu.unideb.bus.ui.CustomAdapter;
import hu.unideb.bus.ui.MarkerInfoWindowWithButtons;
import hu.unideb.bus.utils.SharedPrefKey;
import hu.unideb.bus.utils.SharedPrefUtils;
import hu.unideb.bus.utils.Utils;

public class TripPlannerFragment extends Fragment implements OnMapReadyCallback, OnMapLongClickListener {
    private final String TAG = this.getClass().getSimpleName();

    private AutoCompleteTextView fromPlace;
    private AutoCompleteTextView toPlace;
    private String fromPlaceLocation;
    private String toPlaceLocation;
    private Marker fromMarker;
    private Marker toMarker;
    private MapView mMapView;
    private GoogleMap mMap;

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
        mMap.setInfoWindowAdapter(new MarkerInfoWindowWithButtons(getActivity(), fromPlace, toPlace, fromMarker, toMarker));
        mMap.setOnMapLongClickListener(this);
        Utils.setMapControls(mMap);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.4815, 19.1299), 10));
    }

    @Override
    public void onMapLongClick(LatLng clickedPosition) {
        String address = getAddressFromLocation(clickedPosition);
        if (address == null || address.isEmpty()) {
            return;
        }

        Marker m = mMap.addMarker(new MarkerOptions()
                .position(clickedPosition)
                .title(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_marker)));
        m.showInfoWindow();
        setLocations(clickedPosition);
    }

    private String getAddressFromLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Log.e(TAG, "Geocoder.getFromLocation() failed", e);
        }
        String address = addresses.get(0).getAddressLine(0);
        String substring = address.substring(0, address.lastIndexOf(","));
        return substring;
    }

    private void setLocations(LatLng latLng) {
        fromPlaceLocation = String.format("%f,%f", latLng.latitude, latLng.longitude);
        toPlaceLocation = String.format("%f,%f", latLng.latitude, latLng.longitude);
    }

    private void getItineraries() {
        if (fromPlaceLocation == null || fromPlaceLocation.isEmpty() ||
                toPlaceLocation == null || toPlaceLocation.isEmpty()) {
            Utils.showToast(getActivity(), getResources().getString(R.string.plsWriteSmth), Toast.LENGTH_LONG);
            return;
        }

        SharedPrefUtils.saveString(getActivity(), SharedPrefKey.FROM_PLACE_LOCATION.getKey(), fromPlaceLocation);
        SharedPrefUtils.saveString(getActivity(), SharedPrefKey.TO_PLACE_LOCATION.getKey(), toPlaceLocation);
        startActivity(new Intent(getActivity(), RouteActivity.class));
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
                getItineraries()
        );
    }

    private void setupEditTextListeners() {
        toPlace.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Utils.hideKeyboard(v, getActivity());
                getItineraries();
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
