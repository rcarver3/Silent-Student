package gatech.criminals.silentstudent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // To identify MainActivity in LogCat
    private static final String TAG = "MainActivity";
    public static ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting main activity");
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "Permissions granted");
            }
        });
        setContentView(R.layout.activity_main);
    }

    public void onClickFinish(View view) {
        Log.d(TAG, "Checking permissions");
    }
}