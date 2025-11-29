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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gatech.criminals.silentstudent.databinding.FragmentMainBinding;

/**
 * A fragment representing a list of Items.
 */
public class WifiDetailsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private static final String TAG = "WifiDetailsFragment";
    List<ScanResult> mScanResults;
    private FragmentMainBinding binding;

    private WifiManager mWifiManager;
    private MyWifiDetailsRecyclerViewAdapter mAdapter;
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WifiDetailsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WifiDetailsFragment newInstance(int columnCount) {
        WifiDetailsFragment fragment = new WifiDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate start");
        super.onCreate(savedInstanceState);

        mWifiManager = (WifiManager) requireContext().getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "creating broadcast receiver");
        BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
            @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mScanResults = mWifiManager.getScanResults();
                }
                if (mScanResults != null) {
                    mAdapter.updateData(mScanResults);
                }
            }
        };
        requireContext().registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Log.d(TAG, "registered broadcast receiver as mWifiReceiver");

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView start.\nContainer: " + container);
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated start.\nView: " + view);
        binding.scanWifiButton.setOnClickListener(this::onClickScanWifi);

        // Set the adapter
            Log.d(TAG, "view is RecyclerView, starting creation");
            Context context = view.getContext();
            RecyclerView recyclerView = binding.wifiListRecyclerView;

            mScanResults = new ArrayList<>();
            mAdapter = new MyWifiDetailsRecyclerViewAdapter(mScanResults);
            recyclerView.setAdapter(mAdapter);

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setHasFixedSize(true);
    }

    public void onClickScanWifi(View view) {
        Log.d(TAG, "starting onClickScanWifi");
        if (mWifiManager.isWifiEnabled()) {
            Log.d(TAG, "scanning wifi now");
            mWifiManager.startScan();
        }
    }
}