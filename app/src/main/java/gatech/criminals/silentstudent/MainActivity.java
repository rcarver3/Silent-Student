package gatech.criminals.silentstudent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check and request permissions before trying to get WiFi details
        checkAndRequestLocationPermission();
    }

    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so request it.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission has already been granted, you can get WiFi details now.
            displayWifiInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                Log.d("MainActivity", "Location permission granted.");
                displayWifiInfo();
            } else {
                // Permission was denied. You cannot get SSID/BSSID.
                Log.d("MainActivity", "Location permission denied.");
                Toast.makeText(this, "Location permission is required to get WiFi details.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayWifiInfo() {
        WifiDetailsProvider wifiDetailsProvider = new WifiDetailsProvider(this);
        WifiNetworkInfo wifiInfo = wifiDetailsProvider.getWifiDetails();

        if (wifiInfo != null) {
            Log.i("MainActivity", "Connected to WiFi:");
            Log.i("MainActivity", wifiInfo.toString());
            // You can also update UI elements here
            // e.g., textView.setText(wifiInfo.toString());
        } else {
            Log.w("MainActivity", "Not connected to a WiFi network or cannot retrieve information.");
        }
    }
}