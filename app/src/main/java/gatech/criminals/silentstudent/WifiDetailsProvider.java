package gatech.criminals.silentstudent;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

public class WifiDetailsProvider {

    private final Context context;

    public WifiDetailsProvider(Context context) {
        this.context = context;
    }

    /**
     * Reads information about the currently connected WiFi network.
     *
     * @return a WifiNetworkInfo object or null if not connected or info is unavailable.
     */
    public WifiNetworkInfo getWifiDetails() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Log.e("WifiDetailsProvider", "Could not get ConnectivityManager.");
            return null;
        }

        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            Log.d("WifiDetailsProvider", "No active network.");
            return null;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        if (capabilities == null || !capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.d("WifiDetailsProvider", "Device is not connected to WiFi.");
            return null; // Not connected to WiFi
        }

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            Log.e("WifiDetailsProvider", "Could not get WifiManager.");
            return null;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            Log.d("WifiDetailsProvider", "Could not get WifiInfo.");
            return null;
        }

        // SSID might be "<unknown ssid>" if location permission is missing on newer Android versions. [1]
        String ssid = wifiInfo.getSSID();
        if (ssid != null && !ssid.isEmpty() && !ssid.equals("<unknown ssid>")) {
            // The getSSID() method returns the SSID surrounded by double quotes. [3]
            ssid = ssid.replace("\"", "");
        } else {
            Log.d("WifiDetailsProvider", "SSID is unknown. Check location permissions.");
            return null;
        }

        String bssid = wifiInfo.getBSSID();
        int rssi = wifiInfo.getRssi();

        return new WifiNetworkInfo(ssid, bssid, rssi);
    }
}