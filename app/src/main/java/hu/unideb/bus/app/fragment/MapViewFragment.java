package hu.unideb.bus.app.fragment;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import hu.unideb.bus.R;
import hu.unideb.bus.asynctask.StopTask;
import hu.unideb.bus.room.BusRepository;
import hu.unideb.bus.room.model.RouteEntity;
import hu.unideb.bus.room.model.StopEntity;
import hu.unideb.bus.ui.MarkerInfoWindowAdapter;
import hu.unideb.bus.utils.SharedPrefUtils;
import hu.unideb.bus.utils.Utils;

public class MapViewFragment extends Fragment implements OnMapReadyCallback, OnMyLocationButtonClickListener,
        OnCameraMoveStartedListener, OnCameraIdleListener, OnMarkerClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private final LatLng DEFAULT_LOCATION = new LatLng(47.09222, 17.91232);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 15;
    private Snackbar snackbar;

    private MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private BusRepository mRepoInstance;
    private boolean mLocationPermissionGranted;
    private float mCurrentZoom = 15;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mapview, container, false);
        Utils.setToolbar((AppCompatActivity) getActivity(), this, rootView, R.id.mapViewToolbar);

        mRepoInstance = BusRepository.getInstance(getActivity());
        mMapView = (MapView) rootView.findViewById(R.id.mainMapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "Map initializing failed ", e);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocationPermission();
        setMapUi();
        getLastKnownLocation();
    }


    @Override
    public boolean onMyLocationButtonClick() {
        getLastKnownLocation();
        return true;
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onCameraIdle() {
        if (mMap == null || !SharedPrefUtils.isDbPopulated(getActivity())) {
            Utils.showToast(getActivity(), getResources().getString(R.string.downloadInProgress), Toast.LENGTH_LONG);
            return;
        }

        mCurrentZoom = mMap.getCameraPosition().zoom;
        if (mCurrentZoom < DEFAULT_ZOOM) {
            showSnackBar(getView());
            mMap.clear();
            return;
        } else {
            dismissSnackBar();
        }

        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        LatLng center = visibleRegion.latLngBounds.getCenter();

        StopTask stopTask = new StopTask();
        List<StopEntity> stopsForVisibleRegion = stopTask.getStopsForLocation(center);
        if (stopsForVisibleRegion == null || stopsForVisibleRegion.isEmpty()) {
            return;
        }

        for (StopEntity stop : stopsForVisibleRegion) {
            mRepoInstance.getRoutesForStop(stop.getId()).observe(this, routes ->
                    createStopMarkers(stop, routes)
            );
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
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

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void setMapUi() {
        if (mMap == null) {
            return;
        }
        if (!mLocationPermissionGranted) {
            getLocationPermission();
            return;
        }
        setMapControls();
    }

    @SuppressLint("MissingPermission")
    private void setMapControls() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(getActivity()));
        mMap.setOnMarkerClickListener(this);
    }


    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                moveCamera(location);
            }
        }
    };

    private void moveCamera(Location location) {
        if (location == null) {
            location = new Location("defaultLocation");
            location.setLatitude(DEFAULT_LOCATION.latitude);
            location.setLongitude(DEFAULT_LOCATION.longitude);
        }
        mMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
    }


    private void createStopMarkers(StopEntity stop, List<RouteEntity> routes) {
        Marker m = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(stop.getLat(), stop.getLon()))
                .title(stop.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_marker)));   //todo: marker rotation az iránynak megfelelően?
        m.setTag(routes);
    }

    private void showSnackBar(View view) {
        if (snackbar != null && snackbar.isShown()) {
            return;
        }

        snackbar = Snackbar.make(view, getResources().getString(R.string.plsZoom), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("ZOOM", v ->
                onMyLocationButtonClick()
        );

        TextView text = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        text.setTextColor(Color.WHITE);
        snackbar.show();
    }

    private void dismissSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}
