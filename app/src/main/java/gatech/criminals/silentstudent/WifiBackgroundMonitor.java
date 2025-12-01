package gatech.criminals.silentstudent;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

public class WifiBackgroundMonitor {
    private static final String TAG = "WifiBackgroundMonitor";
    private final Context context;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private NetworkCapabilities mNetworkCapabilities;
    private BroadcastReceiver mPolicyReceiver;

    public WifiBackgroundMonitor(Context context) {
        this.context = context;
    }

    public void startMonitoring() {
        Log.d(TAG, "start monitoring right away!");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest request = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build();
        mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                Log.d(TAG, "network capabilities changed");
                super.onCapabilitiesChanged(network, networkCapabilities);
                mNetworkCapabilities = networkCapabilities;
                updatePhoneVolume();
            }
        };
        cm.registerNetworkCallback(request, mNetworkCallback);

        mPolicyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "policy receiver called!");
                if (NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED.equals(intent.getAction())) {
                    Log.d(TAG, "notification policy access changed!");

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (notificationManager.isNotificationPolicyAccessGranted()) {
                        Log.d(TAG, "Permission granted, retrying silencing...");
                        updatePhoneVolume();
                    }
                }
            }
        };
        context.registerReceiver(mPolicyReceiver, new IntentFilter(NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED));
    }

    public void stopMonitoring() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null && mNetworkCallback != null) {
            try {
                cm.unregisterNetworkCallback(mNetworkCallback);
                mNetworkCallback = null;
                Log.d(TAG, "successfully unregistered network callback!");
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "callback already unregistered or is invalid!", e);
            }
        }

        if (mPolicyReceiver != null) {
            try {
                context.unregisterReceiver(mPolicyReceiver);
                mPolicyReceiver = null;
                Log.d(TAG, "unregistered policy receiver");
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "receiver already unregistered or is invalid", e);
            }
        }
    }

    private void silencePhone() {
        Log.d(TAG, "silencing phone!");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager.isNotificationPolicyAccessGranted()) {
            Log.d(TAG, "notification policy granted");
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            Log.d(TAG, "notification policy not granted!");
            context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void unsilencePhone() {
        Log.d(TAG, "unsilencing phone!");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager.isNotificationPolicyAccessGranted()) {
            Log.d(TAG, "notification policy granted");
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
            Log.d(TAG, "notification policy not granted!");
            context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private void updatePhoneVolume() {
        Log.d(TAG, "update phone volume method called");

        WifiInfo wifiInfo = (WifiInfo) mNetworkCapabilities.getTransportInfo();
        if (wifiInfo != null && wifiInfo.getSSID() != null) {
            String currentSSID = wifiInfo.getSSID();
            String TARGET_SSID = "\"Sister Location\"";
            if (currentSSID.equals(TARGET_SSID)) {
                Log.d(TAG, "target wifi found, currentSSID: " + currentSSID);
                silencePhone();
            } else {
                Log.d(TAG, "not target wifi, currentSSID: " + currentSSID);
                unsilencePhone();
            }
        }
    }
}
