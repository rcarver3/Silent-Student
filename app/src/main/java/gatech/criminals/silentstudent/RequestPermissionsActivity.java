package gatech.criminals.silentstudent;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RequestPermissionsActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String REQUEST_TAG = "RequestPermissionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(REQUEST_TAG, "Starting permission request activity.");
        setContentView(R.layout.activity_request_permissions);
    }

    public void onClickGrantPermission(View view) {
        Log.d(REQUEST_TAG, "Permission granted!");
        finish();
    }

    public void onClickDenyPermission(View view) {
        Log.d(REQUEST_TAG, "Permission denied, giving up.");

        // Intent perm = new Intent();
        // perm.putExtra(MainActivity.GRANTED_EXTRA, false);
        // setResult(RequestPermissionsActivity.RESULT_OK, perm);
        finish();
    }
}