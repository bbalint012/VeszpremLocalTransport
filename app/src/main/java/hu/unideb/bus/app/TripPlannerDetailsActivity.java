package hu.unideb.bus.app;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import hu.unideb.bus.R;
import hu.unideb.bus.app.fragment.RouteChooserFragment;
import hu.unideb.bus.app.fragment.RouteDrawerFragment;
import hu.unideb.bus.utils.SharedPrefKey;
import hu.unideb.bus.utils.SharedPrefUtils;

public class TripPlannerDetailsActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment routeChooserFragment;
    private Fragment routeDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.routeNavigationView);
        fragmentManager = getSupportFragmentManager();
        routeChooserFragment = new RouteChooserFragment();
        routeDrawerFragment = new RouteDrawerFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_results:
            default:
                fragment = routeChooserFragment;
                break;
            case R.id.navigation_routeDraw:
                fragment = routeDrawerFragment;
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.routeActivityContainer, fragment).commit();
        return true;
    };

    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefUtils.remove(this, SharedPrefKey.LEGS);
    }
}
