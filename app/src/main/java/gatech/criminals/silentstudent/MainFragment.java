package gatech.criminals.silentstudent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    public static final String GRANTED_EXTRA = "gatech.criminals.silentstudent.permissions_granted";
    // To identify MainActivity in LogCat
    private static final String MAIN_TAG = "MainActivity";
    // private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static boolean systemPermissionGranted = false;
    // requestPermissionLauncher = registerForActivityResult(requestPermissionsActivity, onActivityResult);
    static ActivityResultLauncher<String> requestPermissionLauncher;
    static ActivityResultLauncher<Intent> permissionRationaleLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MAIN_TAG, "Starting main activity");
        setContentView(R.layout.main_host);
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> systemPermissionGranted = granted);
        permissionRationaleLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                Intent resultData = activityResult.getData();
                if (resultData != null) {
                    boolean permResult = resultData.getBooleanExtra(GRANTED_EXTRA, false);
                    if (permResult) {
                        Log.d(MAIN_TAG, "Permission granted by user, requesting from system");
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {)
}
