package hu.unideb.bus.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.model.Leg;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hu.unideb.bus.R;
import hu.unideb.bus.adapter.RecyclerViewClickListener;
import hu.unideb.bus.adapter.ResultAdapter;
import hu.unideb.bus.app.RouteActivity;
import hu.unideb.bus.utils.SharedPrefUtils;
import hu.unideb.bus.utils.Utils;
import hu.unideb.bus.viewmodel.ResultViewModel;

public class ResultsFragment extends Fragment {
    private ResultAdapter adapter;
    private List<Itinerary> itineraries = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        setupRecyclerView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ResultViewModel mViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        mViewModel.getItineraries().observe(this, itineraries -> {
            if (itineraries.isEmpty()) {
                Utils.showToast(getActivity(), getResources().getString(R.string.sryRouteNotFound), Toast.LENGTH_LONG);
                getActivity().onBackPressed();
                return;
            }
            adapter.setItineraries(itineraries);
        });
    }

    private void setupRecyclerView(View view) {
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.mainRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerViewClickListener listener = this::setOnClickListener;

        adapter = new ResultAdapter(getActivity(), listener, itineraries);
        mRecyclerView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    private void setOnClickListener(View v) {
        final ArrayList<Leg> legs = (ArrayList<Leg>) (v.findViewById(R.id.startTime).getTag());
        SharedPrefUtils.saveItinerary(getActivity(), legs);
        navigateToDrawerFragment();
    }

    private void navigateToDrawerFragment() {
        final RouteActivity activity = (RouteActivity) getActivity();
        activity.getBottomNavigationView().setSelectedItemId(R.id.navigation_routeDraw);
    }
}
