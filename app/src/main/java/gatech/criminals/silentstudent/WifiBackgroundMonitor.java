package gatech.criminals.silentstudent;

import static android.net.ConnectivityManager.NetworkCallback.FLAG_INCLUDE_LOCATION_INFO;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class WifiBackgroundMonitor {
    private static final String TAG = "WifiBackgroundMonitor";
    private static final String CHANNEL_ID = "WifiBackgroundMonitorChannel";
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
        mNetworkCallback = new ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                Log.d(TAG, "network capabilities changed");
                super.onCapabilitiesChanged(network, networkCapabilities);
                mNetworkCapabilities = networkCapabilities;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    stopMonitoring();
                } else {
                    updatePhoneVolume();
                }
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
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            stopMonitoring();
                        } else {
                            updatePhoneVolume();
                        }
                    }
                } else {
                    context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
                }
            }
        };

        context.registerReceiver(mPolicyReceiver, new IntentFilter(NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
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
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle("Phone Silenced!").setContentText("Silent Student has silenced your phone.").setSmallIcon(android.R.drawable.ic_dialog_info).build();
            notificationManager.notify(1, notification);
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
        if (mNetworkCapabilities.getTransportInfo() instanceof @Nullable WifiInfo wifiInfo) {
            if (!Objects.equals(wifiInfo.getSSID(), WifiManager.UNKNOWN_SSID)) {
                String currentSSID = wifiInfo.getSSID();
                Log.d(TAG, "wifiInfo.getSSID(): " + currentSSID);
                String TARGET_SSID = "\"Sister Location\"";
                if (currentSSID.equals(TARGET_SSID)) {
                    Log.d(TAG, "target wifi found, currentSSID: " + currentSSID);
                    silencePhone();
                } else {
                    Log.d(TAG, "not target wifi, currentSSID: " + currentSSID);
                    unsilencePhone();
                }
            }
        } else {
            Log.d(TAG, "mNetworkCapabilities was not of type WifiInfo: " + mNetworkCapabilities);
        }
    }
}
