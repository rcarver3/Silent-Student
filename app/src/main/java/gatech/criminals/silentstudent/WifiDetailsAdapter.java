package gatech.criminals.silentstudent;

import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import gatech.criminals.silentstudent.databinding.WifiDetailsItemLayoutBinding;

/**
 * {@link RecyclerView.Adapter<WifiDetailsAdapter.ViewHolder>} that can display a {@link ScanResult} via {@link ScanResult#getWifiSsid()}
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
        WifiDetailsItemLayoutBinding binding = WifiDetailsItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (mWifiScanResults.isEmpty()) {
            Log.d(TAG, "mWifiScanResults is empty");
            return;
        }

        ScanResult currentScanResult = mWifiScanResults.get(position);
        viewHolder.mSsidTextView.setText(Objects.requireNonNull(currentScanResult.getWifiSsid()).toString().replaceAll("\"", ""));
        viewHolder.mLevelTextView.setText(String.valueOf(currentScanResult.level));
        viewHolder.mLevelTextView.append(" dBm");

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
            newScan.removeIf(result -> Objects.equals(Objects.requireNonNull(result.getWifiSsid()).toString(), ""));
            mWifiScanResults.addAll(newScan);
            notifyItemRangeInserted(0, mWifiScanResults.size());
        }
    }

    @Override
    public int getItemCount() {
        return mWifiScanResults.size();
    }

    public interface OnWifiItemClickListener {
        void onWifiItemClick(ScanResult scanResult);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mSsidTextView;
        public final TextView mLevelTextView;

        public ViewHolder(WifiDetailsItemLayoutBinding binding) {
            super(binding.getRoot());

            mSsidTextView = binding.propertyItem;
            mLevelTextView = binding.valueItem;
        }
    }
}