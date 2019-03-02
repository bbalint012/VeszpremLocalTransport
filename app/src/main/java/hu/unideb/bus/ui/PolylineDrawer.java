package hu.unideb.bus.ui;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.opentripplanner.api.model.Leg;
import org.opentripplanner.routing.core.TraverseMode;

import java.util.List;

import hu.unideb.bus.R;

public class PolylineDrawer {
    private Context context;
    private GoogleMap mMap;
    private float scaleFactor;

    public PolylineDrawer(Context context, GoogleMap mMap) {
        this.context = context;
        this.mMap = mMap;
        this.scaleFactor = context.getResources().getFraction(R.fraction.scaleFactor, 1, 1);
    }

    public void draw(Leg leg, List<LatLng> points) {
        if (TraverseMode.valueOf(leg.mode).isOnStreetNonTransit()) {
            drawNonTransitRoute(points);
            return;
        }

        if (TraverseMode.valueOf(leg.mode).isTransit()) {
            drawTransitRoute(points);
        }
    }

    public void addMarkers(List<LatLng> points) {
        //first
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(points.get(0).latitude, points.get(0).longitude))
                .title("first")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_marker)));

        //last
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(points.get(points.size()-1).latitude, points.get(points.size()-1).longitude))
                .title("first")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_marker)));
    }

    private void drawTransitRoute(List<LatLng> points) {
        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .width(5 * scaleFactor)
                .color(context.getResources().getColor(R.color.blue));
        mMap.addPolyline(options);
    }

    private void drawNonTransitRoute(List<LatLng> points) {
        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .width(5 * scaleFactor)
                .color(context.getResources().getColor(R.color.purple));
        mMap.addPolyline(options);
    }
}
