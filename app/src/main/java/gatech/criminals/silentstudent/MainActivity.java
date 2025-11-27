package gatech.criminals.silentstudent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallerLauncher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends FragmentActivity {
    // To identify MainActivity in LogCat
    private static final String MAIN_TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static ActivityResultLauncher<String> requestPermissionLauncher;

    private static final String GRANTED_EXTRA = "gatech.criminals.silentstudent.granted";
    private boolean locGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MAIN_TAG, "Starting main activity");
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(MAIN_TAG, "You have network access!!");
                locGranted = true;
            }
        });
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.wifi_list_recycler_view, WifiDetailsFragment.class, null)
//                    .commit();
//        }
//        if (!checkPermissions()) {
//            setContentView(R.layout.activity_start);
//        } else {
//            setContentView(R.layout.activity_main);
//        }
    }

//    protected void viewNearbyWifiAP() {
//        if (!checkPermissions()) {
//            Log.d(MAIN_TAG, "Permissions not granted, couldn't complete");
//            getPermissions();
//            return;
//        }
//        Log.d(MAIN_TAG, "Has permissions, displaying wifi info.");
//        wm.getScanResults();
//    }

    public void onClickContinue(View view) {
        Log.d(MAIN_TAG, "Checking permissions");
        getPermissions();
        if (checkPermissions()) {
            setContentView(R.layout.activity_main);
        }
    }
    public void onClickFinish(View view) {
        Log.d(MAIN_TAG, "Checking permissions");
    }

    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(MAIN_TAG, "Has permissions, displaying wifi info.");
            return true;
        }
        return false;
    }
    public void getPermissions() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d(MAIN_TAG, "starting permissions rationale dialog");
            new PermissionsRationaleFragment().show(getSupportFragmentManager(), PermissionsRationaleFragment.REQUEST_TAG);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

//    private void checkAndGetPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Log.d(MAIN_TAG, "Has permissions, displaying wifi info.");
//            locGranted = true;
//        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Log.d(MAIN_TAG, "Showing rationale now");
//                permissionRationaleLauncher.launch(new Intent(this, RequestPermissionsActivity.class));
//            } else {
//                Log.d(MAIN_TAG, "asking for permissions");
//                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//        }
//        if (locGranted) {
//            Toast.makeText(this, "You have network access!!", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Location permission is required to use Silent Student.", Toast.LENGTH_LONG).show();
//        }
//    }
}