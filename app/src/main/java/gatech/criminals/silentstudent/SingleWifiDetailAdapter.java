package gatech.criminals.silentstudent;

import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gatech.criminals.silentstudent.databinding.WifiDetailsItemLayoutBinding;

public class SingleWifiDetailAdapter extends RecyclerView.Adapter<SingleWifiDetailAdapter.ViewHolder> {
    private static final String TAG = "SingleWifiDetailAdapter";
    private final ScanResult mResult;
    private final List<String> mResultProperties = List.of("SSID", "BSSID", "Level", "Frequency", "Timestamp", "Channel Width");
    private ArrayList<String> mResultValues;

    public SingleWifiDetailAdapter(ScanResult result) {
        mResult = result;
        initResultProperties();
    }

    private void initResultProperties() {
        Log.d(TAG, "initializing wifi properties now");
        if (mResult == null) {
            Log.d(TAG, "mResult is null");
            return;
        }

        mResultValues = new ArrayList<>();
        mResultValues.add(String.valueOf(mResult.getWifiSsid()).replaceAll("\"", ""));
        mResultValues.add(mResult.BSSID);
        mResultValues.add(mResult.level + " dBm");
        mResultValues.add(mResult.frequency + " MHz");
        mResultValues.add(String.valueOf(mResult.timestamp));
        mResultValues.add(String.valueOf(mResult.channelWidth));

        if (mResultProperties.size() != mResultValues.size()) {
            Log.d(TAG, "mResultProperties and mResultValues are not the same size.");
            Log.d(TAG, "mResultProperties: " + mResultProperties.size());
            Log.d(TAG, mResultProperties.toString());
            Log.d(TAG, "mResultValues: " + mResultValues.size());
            Log.d(TAG, mResultValues.toString());
            throw new ArrayStoreException("mResultProperties and mResultValues are not the same size");
        }
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
        if (mResult == null) {
            Log.d(TAG, "mResult is null");
        }

        Log.d(TAG, "setting property: " + mResultProperties.get(position) + " to value: " + mResultValues.get(position));
        viewHolder.mPropertyTextView.setText(mResultProperties.get(position));
        viewHolder.mValueTextView.setText(mResultValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mResultProperties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mPropertyTextView;
        public final TextView mValueTextView;

        public ViewHolder(WifiDetailsItemLayoutBinding binding) {
            super(binding.getRoot());

            mPropertyTextView = binding.propertyItem;
            mValueTextView = binding.valueItem;
        }
    }
}
