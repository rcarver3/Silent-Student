package gatech.criminals.silentstudent;

import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gatech.criminals.silentstudent.databinding.WifiDetailsItemLayoutBinding;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ScanResult} via its {@link ScanResult#SSID}
 * and its {@link ScanResult#BSSID}.
 */
public class WifiDetailsAdapter extends RecyclerView.Adapter<WifiDetailsAdapter.ViewHolder> {
    private static final String TAG = "WifiDetailsAdapter";
    private final List<ScanResult> mWifiScanResults;

    public WifiDetailsAdapter(List<ScanResult> items) {
        mWifiScanResults = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder start");
        WifiDetailsItemLayoutBinding mBinding = WifiDetailsItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder start");
        if (mWifiScanResults.isEmpty()) {
            Log.d(TAG, "mWifiScanResults is empty");
            return;
        }

        Log.d(TAG, "setting ssid and bssid at position: " + position);
        ScanResult currentScanResult = mWifiScanResults.get(position);
        viewHolder.mSsidTextView.setText(currentScanResult.SSID);
        viewHolder.mBssidTextView.setText(currentScanResult.BSSID);
    }

    public void updateData(List<ScanResult> newScan) {
        Log.d(TAG, "updateData start");
        int itemCount = mWifiScanResults.size();
        mWifiScanResults.clear();
        notifyItemRangeRemoved(0, itemCount);

        if ((newScan != null) && (!newScan.isEmpty())) {
            mWifiScanResults.addAll(newScan);
            notifyItemRangeInserted(0, mWifiScanResults.size());
        }
    }

    @Override
    public int getItemCount() {
        return mWifiScanResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSsidTextView;
        public TextView mBssidTextView;
        // TODO: maybe signal strength indicator is more interesting?

        public ViewHolder(WifiDetailsItemLayoutBinding binding) {
            super(binding.getRoot());

            mSsidTextView = binding.ssidWifiItem;
            mBssidTextView = binding.bssidWifiItem;
        }
    }
}