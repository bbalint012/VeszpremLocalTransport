package hu.unideb.bus.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.opentripplanner.api.model.Leg;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import hu.unideb.bus.R;
import hu.unideb.bus.ui.PolylineDrawer;
import hu.unideb.bus.utils.LocationUtil;
import hu.unideb.bus.utils.SharedPrefUtils;
import hu.unideb.bus.utils.Utils;

public class RouteDrawerFragment extends Fragment implements OnMapReadyCallback {
    private final String TAG = this.getClass().getSimpleName();
    private MapView mMapView;
    private GoogleMap mMap;
    private PolylineDrawer polylineDrawer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_drawer, container, false);
        Utils.setToolbar((AppCompatActivity) getActivity(), this, view, R.id.routeDrawerToolbar);
        mMapView = (MapView) view.findViewById(R.id.routeDrawerMapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "Map initializing failed ", e);
        }
        mMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        polylineDrawer = new PolylineDrawer(getActivity(), mMap);
        Utils.setMapControls(getActivity(), mMap);
        showRouteOnMap();
    }

    @SuppressWarnings("unchecked")
    private void showRouteOnMap() {
        final List<Leg> itinerary = getItinerary();
        if (itinerary.isEmpty()) {
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

    private List<Leg> getItinerary() {
        return SharedPrefUtils.getItinerary(getActivity());
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
