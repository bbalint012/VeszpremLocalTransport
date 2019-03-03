package hu.unideb.bus.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import hu.unideb.bus.R;

public class MarkerInfoWindowWithButtons implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private AutoCompleteTextView fromTv;
    private AutoCompleteTextView toTv;

    public MarkerInfoWindowWithButtons(Context context, AutoCompleteTextView fromTv, AutoCompleteTextView toTv, Marker fromMarker, Marker toMarker) {
        this.context = context;
        this.fromTv = fromTv;
        this.toTv = toTv;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marker_info_window_with_buttons, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        setSnippet(view, marker.getTitle());
        return view;
    }

    private void setSnippet(View view, String address) {
        Button travelFromBtn = (Button) view.findViewById(R.id.travelFromBtn);
        Button travelToBtn = (Button) view.findViewById(R.id.travelToBtn);

        travelFromBtn.setOnClickListener(v -> fromTv.setText(address));
        travelToBtn.setOnClickListener(v -> toTv.setText(address));
    }
}
