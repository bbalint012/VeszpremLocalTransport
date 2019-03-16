package hu.unideb.bus.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.opentripplanner.api.model.Leg;
import org.opentripplanner.routing.core.TraverseMode;

import java.util.List;

import hu.unideb.bus.R;

public class PolylineDrawer {
    private final static int MARKER_ICON_SIZE = 50;
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
            drawRoute(points, context.getResources().getColor(R.color.purple));
            addMarkers(leg, points, R.drawable.point_grey);
        } else if (TraverseMode.valueOf(leg.mode).isTransit()) {
            drawRoute(points, context.getResources().getColor(R.color.blue));
            addMarkers(leg, points, R.drawable.point_blue);
        }
    }

    private void addMarkers(Leg leg, List<LatLng> points, int iconId) {
        final BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons(iconId));

        //first
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(points.get(0).latitude, points.get(0).longitude))
                .title(leg.from.getName())
                .icon(icon));

        //last
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude))
                .title(leg.to.getName())
                .icon(icon));
    }

    private Bitmap resizeMapIcons(int iconId) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), iconId);
        return Bitmap.createScaledBitmap(imageBitmap, MARKER_ICON_SIZE, MARKER_ICON_SIZE, false);
    }

    private void drawRoute(List<LatLng> points, int color) {
        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .width(5 * scaleFactor)
                .color(color);
        mMap.addPolyline(options);
    }
}
