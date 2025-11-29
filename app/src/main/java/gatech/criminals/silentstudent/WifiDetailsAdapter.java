package gatech.criminals.silentstudent;

import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ScanResult} via its {@link ScanResult#SSID}
 * and its {@link ScanResult#BSSID}.
 */
public class WifiDetailsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "WifiDetailsAdapter";
    private final List<ScanResult> mWifiScanResults;

    public WifiDetailsAdapter(List<ScanResult> items) {
        mWifiScanResults = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder start");
        return new ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_details_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder start");
        if (holder instanceof ViewHolderItem viewHolderItem) {
            if (mWifiScanResults.isEmpty()) {
                Log.d(TAG, "mWifiScanResults is empty");
                return;
            }

            Log.d(TAG, "setting ssid and bssid at position: " + position);
            ScanResult currentScanResult = mWifiScanResults.get(position);
            viewHolderItem.mSsidTextView.setText(currentScanResult.SSID);
            viewHolderItem.mBssidTextView.setText(currentScanResult.BSSID);
        } else {
            throw new RuntimeException(holder + " isn't a valid scan result or view holder.");
        }
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
        return mWifiScanResults.size() + 1;
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        public TextView mSsidTextView;
        public TextView mBssidTextView;

        public ViewHolderItem(View view) {
            super(view);
            mSsidTextView = view.findViewById(R.id.ssid_wifi_item);
            mBssidTextView = view.findViewById(R.id.bssid_wifi_item);
        }
    }
}