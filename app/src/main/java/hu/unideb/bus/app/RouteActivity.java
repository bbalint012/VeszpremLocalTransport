package hu.unideb.bus.app;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import hu.unideb.bus.R;
import hu.unideb.bus.app.fragment.ResultsFragment;
import hu.unideb.bus.app.fragment.RouteDrawerFragment;
import hu.unideb.bus.utils.SharedPrefKey;
import hu.unideb.bus.utils.SharedPrefUtils;

public class RouteActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment resultsFragment;
    private Fragment routeDrawFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.routeNavigationView);
        fragmentManager = getSupportFragmentManager();
        resultsFragment = new ResultsFragment();
        routeDrawFragment = new RouteDrawerFragment();
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
                fragment = resultsFragment;
                break;
            case R.id.navigation_routeDraw:
                fragment = routeDrawFragment;
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
