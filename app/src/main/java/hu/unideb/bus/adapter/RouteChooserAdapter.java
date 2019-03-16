package hu.unideb.bus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nex3z.flowlayout.FlowLayout;

import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.model.Leg;
import org.opentripplanner.routing.core.TraverseMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hu.unideb.bus.R;

public class RouteChooserAdapter extends RecyclerView.Adapter<RouteChooserViewHolder> {
    private final int INFO_ICON_SIZE;
    private final Context context;
    private final RecyclerViewClickListener listener;
    private List<Itinerary> itineraries;

    public RouteChooserAdapter(Context context, RecyclerViewClickListener listener, List<Itinerary> itineraries) {
        this.context = context;
        this.listener = listener;
        this.itineraries = itineraries;
        INFO_ICON_SIZE = (int) context.getResources().getDimension(R.dimen.info_icon_size);
    }

    @NonNull
    @Override
    public RouteChooserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);
        itemView.setFocusable(true);
        return new RouteChooserViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteChooserViewHolder holder, int position) {
        final Itinerary item = itineraries.get(position);
        holder.startTime.setTag(item.legs);

        holder.startTime.setText(formatDate(item.startTime));
        holder.endTime.setText(formatDate(item.endTime));
        holder.walkDistance.setText(String.format("%d", item.walkDistance.intValue()));
        holder.duration.setText(String.format("%d", item.duration / 60));

        Iterator<Leg> iterator = item.legs.iterator();
        while (iterator.hasNext()) {
            setInfoIconsLayout(holder.infoIconsLayout, iterator.next().mode, iterator.hasNext());
        }
    }

    @Override
    public int getItemCount() {
        return itineraries == null ? 0 : itineraries.size();
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
        notifyDataSetChanged();
    }

    private String formatDate(String millis) {
        Date date = new Date(Long.valueOf(millis));
        DateFormat df = new SimpleDateFormat("HH:mm", new Locale("hu"));
        return df.format(date);
    }


    private void setInfoIconsLayout(FlowLayout infoLayout, String travelMode, boolean hasNext) {
        if (TraverseMode.valueOf(travelMode).isTransit()) {
            infoLayout.addView(makeImageView(R.drawable.ic_bus));
        } else {
            infoLayout.addView(makeImageView(R.drawable.ic_walk));
        }

        if (hasNext) {
            infoLayout.addView(makeImageView(R.drawable.ic_next));
        }
    }

    private ImageView makeImageView(int imgId) {
        ImageView imageview = new ImageView(context);
        FlowLayout.LayoutParams params = new FlowLayout
                .LayoutParams(INFO_ICON_SIZE, INFO_ICON_SIZE);
        imageview.setLayoutParams(params);
        imageview.setImageResource(imgId);
        return imageview;
    }
}
