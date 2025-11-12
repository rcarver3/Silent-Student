package gatech.criminals.silentstudent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    // To identify MainActivity in LogCat
    private static final String MAIN_TAG = "MainActivity";
    private static final String REQUEST_TAG = "RequestPermissionsActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REPEAT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MAIN_TAG, "onCreate()");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Start activity requesting permission if system determines it
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(MAIN_TAG, "Showing permission request activity.");
                setContentView(R.layout.activity_request_permissions);
            } else {
                requestAppPermissions(this, MAIN_TAG, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // Show main activity with WiFi details
            Log.d(MAIN_TAG, "Has permissions, Showing main activity.");
            setContentView(R.layout.activity_main);
            displayWifiInfo();
        }
    }

    public void onClickGrantPermission(View view) {
        requestAppPermissions(this, REQUEST_TAG, LOCATION_PERMISSION_REPEAT_REQUEST_CODE);
    }

    public void onClickDenyPermission(View view) {
        Log.d(REQUEST_TAG, "Permission denied, giving up.");
        Toast.makeText(this, "Location permission is required to use Silent Student.", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                Log.d(MAIN_TAG, "Location permission granted.");
                displayWifiInfo();
            } else {
                // Permission was denied or cancelled.
                Log.d(MAIN_TAG, "Location permission denied, first time");
                setContentView(R.layout.activity_request_permissions);
            }
        }
        if (requestCode == LOCATION_PERMISSION_REPEAT_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(REQUEST_TAG, "finally obtained permissions!");
                displayWifiInfo();
            } else {
                Log.d(REQUEST_TAG, "Location permission denied again, giving up :(");
                Toast.makeText(this, "Location permission is required to use Silent Student.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private static void requestAppPermissions(AppCompatActivity activity, String tag, int requestCode) {
        Log.d(tag, "Requesting permission now");
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

    private void displayWifiInfo() {
        WifiDetailsProvider wifiDetailsProvider = new WifiDetailsProvider(this);
        WifiNetworkInfo wifiInfo = wifiDetailsProvider.getWifiDetails();

        if (wifiInfo != null) {
            Log.i(MAIN_TAG, "Connected to WiFi:");
            Log.i(MAIN_TAG, wifiInfo.toString());
            // You can also update UI elements here
            // e.g., textView.setText(wifiInfo.toString());
        } else {
            Log.w(MAIN_TAG, "Not connected to a WiFi network or cannot retrieve information.");
        }
    }
}