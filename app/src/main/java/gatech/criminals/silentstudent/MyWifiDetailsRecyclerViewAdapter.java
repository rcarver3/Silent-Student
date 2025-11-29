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
 * {@link RecyclerView.Adapter} that can display a {@link ScanResult}.
 */
public class MyWifiDetailsRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "WifiDetailsAdapter";
    private static final int HEADER_POSITION = 0;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final List<ScanResult> mWifiScanResults;

    public MyWifiDetailsRecyclerViewAdapter(List<ScanResult> items) {
        mWifiScanResults = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder start");
        ViewHolder viewHolder;

        if (viewType == TYPE_HEADER) {
            Log.d(TAG, "recycler view header");
            viewHolder = new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_details_item_heading_layout, parent, false));
        } else if (viewType == TYPE_ITEM) {
            Log.d(TAG, "recycler view item");
            viewHolder = new ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_details_item_layout, parent, false));
        } else {
            throw new RuntimeException("There is no type that matches the type " + viewType + " \n");
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder start");
        if (!(holder instanceof ViewHolderHeader)) {
            if (holder instanceof ViewHolderItem viewHolderItem) {
                Log.d(TAG, "ViewHolder is item");
                ScanResult currentScanResult = mWifiScanResults.get(position - 1);

                viewHolderItem.mSsidTextView.setText(currentScanResult.SSID);
                viewHolderItem.mBssidTextView.setText(currentScanResult.BSSID);
            } else {
                throw new RuntimeException(holder + " isn't a valid scan result or view holder.");
            }
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

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        public ViewHolderHeader(View view) {
            super(view);
        }
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