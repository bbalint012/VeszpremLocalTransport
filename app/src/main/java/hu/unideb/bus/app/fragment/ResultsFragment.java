package hu.unideb.bus.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import hu.unideb.bus.R;
import hu.unideb.bus.viewmodel.ResultViewModel;

public class ResultsFragment extends Fragment {
    /*private final int INFO_ICON_SIZE = (int) getResources().getDimension(R.dimen.info_icon_size);
    private final int INFO_ICON_MARGIN_END = (int) getResources().getDimension(R.dimen.info_icon_margin_end);*/
    private LinearLayout rootLayout;

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        rootLayout = (LinearLayout) view.findViewById(R.id.resultContainer);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ResultViewModel mViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        mViewModel.getItineraries().observe(this, this::showResults);
    }

    private void showResults(List<Itinerary> itineraries) {
        for (Itinerary item : itineraries) {
            ConstraintLayout childLayout = createChildLayout(item);
            rootLayout.addView(childLayout);
        }
    }

    private ConstraintLayout createChildLayout(Itinerary item) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ConstraintLayout childLayout = (ConstraintLayout) inflater.inflate(R.layout.result_list_item, null, false);
        TextView startTime = (TextView) childLayout.findViewById(R.id.startTime);
        TextView endTime = (TextView) childLayout.findViewById(R.id.endTime);
        TextView walkDistance = (TextView) childLayout.findViewById(R.id.walkDistance);
        TextView duration = (TextView) childLayout.findViewById(R.id.duration);
        LinearLayout infoIconsLayout = (LinearLayout) childLayout.findViewById(R.id.infoIconsLayout);

        startTime.setText(formatDate(item.startTime));
        endTime.setText(formatDate(item.endTime));
        walkDistance.setText(String.format("%d", item.walkDistance.intValue()));
        duration.setText(String.format("%d", item.duration / 60));

        Iterator<Leg> iterator = item.legs.iterator();
        while (iterator.hasNext()) {
            setInfoIconsLayout(infoIconsLayout, iterator.next().mode, iterator.hasNext());
        }

        return childLayout;
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
        ImageView imageview = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(30, 30);
        params.setMarginEnd(10);
        imageview.setLayoutParams(params);
        imageview.setImageResource(imgId);
        return imageview;
    }
}
