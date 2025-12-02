package gatech.criminals.silentstudent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.color.DynamicColors;

/**
 * Main {@link Activity} that hosts the main fragment {@link WifiDetailsFragment}
 * and logic regarding the fragment.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Starting main activity");
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());
        setContentView(R.layout.activity_main);

    }
}