package gatech.criminals.silentstudent;

import static gatech.criminals.silentstudent.MainActivity.requestPermissionLauncher;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class PermissionsRationaleFragment extends DialogFragment {
    public static final String REQUEST_TAG = "RequestPermissionsActivity";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.permission_rationale))
                .setPositiveButton(getString(R.string.continue_dialog), (dialog, which) -> {
                    Log.d(REQUEST_TAG, "show permissions dialog");
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                } )
                .create();
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(REQUEST_TAG, "Starting permission request activity.");
//        setContentView(R.layout.activity_request_permissions);
//    }

//    public void onClickGrantPermission(View view) {
//        Log.d(REQUEST_TAG, "Permission granted!");
//        finish();
//    }

//    public void onClickDenyPermission(View view) {
//        Log.d(REQUEST_TAG, "Permission denied, giving up.");
//
//        // Intent perm = new Intent();
//        // perm.putExtra(MainActivity.GRANTED_EXTRA, false);
//        // setResult(RequestPermissionsActivity.RESULT_OK, perm);
//        finish();
//    }
}