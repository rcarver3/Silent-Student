package gatech.criminals.silentstudent;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gatech.criminals.silentstudent.databinding.FragmentSingleWifiDetailBinding;

public class SingleWifiDetailFragment extends Fragment {
    private static final String TAG = "SingleWifiDetailFragment";
    private static final String ARG_RESULT = "result";
    private FragmentSingleWifiDetailBinding mBinding;
    private ScanResult mResult;

    public static SingleWifiDetailFragment newInstance(ScanResult result) {
        SingleWifiDetailFragment fragment = new SingleWifiDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView start.\nContainer: " + container);
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = FragmentSingleWifiDetailBinding.inflate(getLayoutInflater(), container, false);

        if (getArguments() != null) {
            Log.d(TAG, "arguments are not null.\nScan Result: " + mResult);
            mResult = getArguments().getParcelable(ARG_RESULT, ScanResult.class);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated start.\nView: " + view);
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "setting ssid title");
        mBinding.ssidTitle.setText(String.valueOf(mResult.getWifiSsid()).replaceAll("\"", ""));

        Log.d(TAG, "setting on click listener");
        mBinding.addToKnownWifiList.setOnClickListener(v -> onClickAddToKnownWifi());

        Log.d(TAG, "setting up recycler view");
        RecyclerView recyclerView = mBinding.propertyList;
        SingleWifiDetailAdapter adapter = new SingleWifiDetailAdapter(mResult);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void onClickAddToKnownWifi() {
        Log.d(TAG, "onClickAddToKnownWifi for ssid: " + mResult.getWifiSsid());
        getParentFragmentManager().popBackStack();
        // TODO: add to known wifi list
    }
}
