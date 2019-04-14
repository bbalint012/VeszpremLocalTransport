package hu.unideb.bus.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import hu.unideb.bus.R;
import hu.unideb.bus.asynctask.PopulateDbAsyncTask;
import hu.unideb.bus.utils.SharedPrefUtils;

import static hu.unideb.bus.utils.SharedPrefUtils.clearSharedPreferences;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //populate the DB at the first launch
        if (!SharedPrefUtils.isDbPopulated(this)) {
            new PopulateDbAsyncTask(this).execute();
        }

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 500);
    }
}