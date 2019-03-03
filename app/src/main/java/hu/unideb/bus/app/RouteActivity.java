package hu.unideb.bus.app;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.opentripplanner.api.model.Itinerary;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import hu.unideb.bus.R;
import hu.unideb.bus.app.fragment.ResultsFragment;
import hu.unideb.bus.app.fragment.RouteDrawerFragment;

public class RouteActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment resultsFragment;
    private Fragment routeDrawFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.routeNavigationView);
        fragmentManager = getSupportFragmentManager();
        resultsFragment = new ResultsFragment();
        routeDrawFragment = new RouteDrawerFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_results:
            default:
                fragment = resultsFragment;
                break;
            case R.id.navigation_routeDraw:
                fragment = routeDrawFragment;
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.routeActivityContainer, fragment).commit();
        return true;
    };
}
