package gatech.criminals.silentstudent;

public class WifiNetworkInfo {
    private final String ssid;
    private final String bssid;
    private final int signalStrength; // RSSI value in dBm

    public WifiNetworkInfo(String ssid, String bssid, int signalStrength) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.signalStrength = signalStrength;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    @Override
    public String toString() {
        return "SSID: " + ssid + '\n' +
                "BSSID: " + bssid + '\n' +
                "Signal Strength (RSSI): " + signalStrength + " dBm";
    }
}