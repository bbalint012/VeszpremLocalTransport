package hu.unideb.bus.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

import hu.unideb.bus.R;
import hu.unideb.bus.room.model.RouteEntity;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
    private Context context;

    public MarkerInfoWindowAdapter(Context context) {
        this.context = context;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.marker_info_window, null);

        TextView title = (TextView) v.findViewById(R.id.title);
        FlowLayout snippetLayout = (FlowLayout) v.findViewById(R.id.snippetLayout);
        title.setText(marker.getTitle());

        Object tag = marker.getTag();
        List<RouteEntity> routes = castTag(tag);
        if (routes != null) {
            setSnippet(routes, snippetLayout);
        }
        return v;
    }

    private void setSnippet(List<RouteEntity> routes, FlowLayout layout) {
        layout.removeAllViews();

        for (RouteEntity route : routes) {
            TextView tv = new TextView(context);
            tv.setText(route.getShortName());
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundResource(R.drawable.rounded_corner);
            layout.addView(tv);
        }
    }

    @SuppressWarnings("unchecked")
    private List<RouteEntity> castTag(Object tag) {
        if (!(tag instanceof List<?>)) {
            return null;
        }
        List<?> listTag = (List)tag;
        if (listTag.isEmpty() || !(listTag.get(0) instanceof RouteEntity)) {
            return null;
        }

        return (List<RouteEntity>) tag;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //TODO
    }
}
