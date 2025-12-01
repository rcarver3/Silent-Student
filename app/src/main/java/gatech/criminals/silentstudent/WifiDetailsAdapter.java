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
 * {@link RecyclerView.Adapter} that can display a {@link ScanResult} via {@link ScanResult#getWifiSsid()}
 * and its {@link ScanResult#BSSID}.
 */
public class WifiDetailsAdapter extends RecyclerView.Adapter<WifiDetailsAdapter.ViewHolder> {
    private static final String TAG = "WifiDetailsAdapter";
    private final List<ScanResult> mWifiScanResults;
    private final OnWifiItemClickListener mListener;

    public WifiDetailsAdapter(List<ScanResult> items, OnWifiItemClickListener listener) {
        mWifiScanResults = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder start");
        WifiDetailsItemLayoutBinding binding = WifiDetailsItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
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

        viewHolder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onWifiItemClick(currentScanResult);
            }
        });
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

            mSsidTextView = binding.propertyItem;
            mBssidTextView = binding.valueItem;
        }
    }

    public interface OnWifiItemClickListener {
        void onWifiItemClick(ScanResult scanResult);
    }
}