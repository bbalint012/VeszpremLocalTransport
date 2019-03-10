package hu.unideb.bus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.model.Leg;
import org.opentripplanner.routing.core.TraverseMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hu.unideb.bus.R;

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {
    private final Context context;
    private final RecyclerViewClickListener listener;
    private List<Itinerary> itineraries;

    public ResultAdapter(Context context, RecyclerViewClickListener listener, List<Itinerary> itineraries) {
        this.context = context;
        this.listener = listener;
        this.itineraries = itineraries;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);
        itemView.setFocusable(true);
        return new ResultViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
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


    private void setInfoIconsLayout(LinearLayout infoLayout, String travelMode, boolean hasNext) {
        if (infoLayout.getChildCount() > 0) {
            infoLayout.removeAllViews();
        }

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
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(30, 30);
        params.setMarginEnd(100);
        imageview.setLayoutParams(params);
        imageview.setImageResource(imgId);
        return imageview;
    }
}
