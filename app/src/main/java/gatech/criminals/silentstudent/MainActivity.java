package gatech.criminals.silentstudent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
                              /**
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
    */

    public void onClickFinish(View view) {
        Log.d(MAIN_TAG, "Checking permissions");
        checkAndGetPermission();
    }

    private void checkAndGetPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(MAIN_TAG, "Has permissions, displaying wifi info.");
            systemPermissionGranted = true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(MAIN_TAG, "Showing rationale now");
                permissionRationaleLauncher.launch(new Intent(this, RequestPermissionsActivity.class));
            } else {
                Log.d(MAIN_TAG, "asking for permissions");
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        if (systemPermissionGranted) {
            Toast.makeText(this, "You have network access!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Location permission is required to use Silent Student.", Toast.LENGTH_LONG).show();
        }
    }
}