package hu.unideb.bus.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hu.unideb.bus.R;

public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private RecyclerViewClickListener listener;

    protected TextView startTime;
    protected TextView endTime;
    protected TextView walkDistance;
    protected TextView duration;
    protected LinearLayout infoIconsLayout;

    ResultViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        initializeItems(itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }

    private void initializeItems(View view) {
        startTime = (TextView) view.findViewById(R.id.startTime);
        endTime = (TextView) view.findViewById(R.id.endTime);
        walkDistance = (TextView) view.findViewById(R.id.walkDistance);
        duration = (TextView) view.findViewById(R.id.duration);
        infoIconsLayout = (LinearLayout) view.findViewById(R.id.infoIconsLayout);
    }
}
