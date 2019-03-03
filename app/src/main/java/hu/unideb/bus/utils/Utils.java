package hu.unideb.bus.utils;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import hu.unideb.bus.R;

public class Utils {
    private static Toast toast;

    public static void showToast(Context context, String message, int toastDuration) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(context, message, toastDuration);
            toast.show();
        }
    }

    public static void setToolbar(AppCompatActivity activity, Fragment fragment, View view, int toolbarId) {
        fragment.setHasOptionsMenu(true);
        Toolbar mToolbar = (Toolbar) view.findViewById(toolbarId);
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public static void setMapControls(GoogleMap map) {
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
    }

    public static int getActionBarHeight(Context context) {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        return result;
    }

    public static void enableTouch(ConstraintLayout parentLayout, boolean isEnabled) {
        if (isEnabled) {
            parentLayout.setAlpha(1.0f);
        } else {
            parentLayout.setAlpha(0.3f);
        }
        parentLayout.setEnabled(isEnabled);
    }

    public static void showKeyboard(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
