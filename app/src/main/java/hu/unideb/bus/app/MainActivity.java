package hu.unideb.bus.app;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import hu.unideb.bus.R;
import hu.unideb.bus.app.fragment.MapViewFragment;
import hu.unideb.bus.app.fragment.TripPlannerFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment mapViewFragment;
    private Fragment tripPlaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        mapViewFragment = new MapViewFragment();
        tripPlaFragment = new TripPlannerFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_map:
            default:
                fragment = mapViewFragment;
                break;
            case R.id.navigation_planner:
                fragment = tripPlaFragment;
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.mainActivityContainer, fragment).commit();
        return true;
    };
}
