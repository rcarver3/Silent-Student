package gatech.criminals.silentstudent;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gatech.criminals.silentstudent.databinding.FragmentMainBinding;

/**
 * Main {@link Fragment} that shows the title, scan Wifi button, and the RecyclerView that holds
 * the details about the surrounding Wifi networks.
 */
public class WifiDetailsFragment extends Fragment implements PermissionsRationaleFragment.RationaleDialogListener {
    private static final String TAG = "WifiDetailsFragment";
    public static ActivityResultLauncher<String> requestPermissionLauncher;
    List<ScanResult> mScanResults;
    private FragmentMainBinding mBinding;
    private WifiManager mWifiManager;
    private WifiDetailsAdapter mWifiDetailsAdapter;
    private BroadcastReceiver mWifiReceiver;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WifiDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate start");
        super.onCreate(savedInstanceState);

        mWifiManager = (WifiManager) requireContext().getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "creating broadcast receiver");
        mWifiReceiver = new BroadcastReceiver() {
            @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "scan results received");
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "fragment has permission, getting scan results");
                    mScanResults = mWifiManager.getScanResults();
                    mScanResults.removeIf(result -> result.SSID.isEmpty());
                } else {
                    Log.d(TAG, "fragment does not have permission!");
                }
                if (mScanResults != null) {
                    Log.d(TAG, "mScanResults is not null, updating data");
                    mWifiDetailsAdapter.updateData(mScanResults);
                } else {
                    Log.d(TAG, "mScanResults was null!");
                }
            }
        };
        requireContext().registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Log.d(TAG, "registered broadcast receiver as mWifiReceiver");

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "Permissions granted, attempting scan now");
                startWifiScan();
            } else {
                Log.d(TAG, "Permission denied by user");
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView start.\nContainer: " + container);
        mBinding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated start.\nView: " + view);
        mBinding.scanWifiButton.setOnClickListener(v -> onClickScanWifi());

        // Set the adapter
        Log.d(TAG, "view is RecyclerView, starting creation");
        Context context = view.getContext();
        RecyclerView recyclerView = mBinding.wifiListRecyclerView;

        mScanResults = new ArrayList<>();
        mWifiDetailsAdapter = new WifiDetailsAdapter(mScanResults, result -> {
            Log.d(TAG, "Clicked on: " + result.SSID);
            openDetailFragment(result);
        });
        recyclerView.setAdapter(mWifiDetailsAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
    }

    private void openDetailFragment(ScanResult result) {
        SingleWifiDetailFragment fragment = SingleWifiDetailFragment.newInstance(result);
        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_host, fragment).addToBackStack(null).commit();
    }

    public void onClickScanWifi() {
        Log.d(TAG, "starting onClickScanWifi");
        startWifiScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWifiReceiver != null) {
            requireContext().unregisterReceiver(mWifiReceiver);
            Log.d(TAG, "unregistered mWifiReceiver");
        }
    }

    private void startWifiScan() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mWifiManager.isWifiEnabled()) {
                Log.d(TAG, "scanning wifi now");
                if (!mWifiManager.startScan()) {
                    Log.w(TAG, "Wifi scan did not start, try again soon");
                } else {
                    Log.d(TAG, "wifi scan started successfully!");
                }
            } else {
                Log.d(TAG, "wifi is not enabled!! Start it now!!");
            }
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d(TAG, "Showing permission rationale dialog");
            PermissionsRationaleFragment dialog = new PermissionsRationaleFragment();
            dialog.show(getChildFragmentManager(), "PermissionsRationaleFragment");
        } else {
            Log.d(TAG, "Requesting permission for first time (or selected don't ask again_)");
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.d(TAG, "user clicked continue from rationale dialog, requesting permissions now");
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }
}