package gatech.criminals.silentstudent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class WifiBackgroundMonitorService extends Service {
    private static final String TAG = "WifiBackgroundMonitorService";
    private static final String CHANNEL_ID = "WifiBackgroundMonitorChannel";
    private WifiBackgroundMonitor mMonitor;

    @Override
    public void onCreate() {
        Log.d(TAG, "service created!");
        super.onCreate();
        createNotificationChannel();

        mMonitor = new WifiBackgroundMonitor(this);
        mMonitor.startMonitoring();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "start command received!");
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Silent Student Active").setContentText("Monitoring WiFi to silence phone...").setSmallIcon(android.R.drawable.ic_dialog_info).build();
        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMonitor.stopMonitoring();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // not needed
    }

    private void createNotificationChannel() {
        Log.d(TAG, "creating notification channel now!");
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Silent Student Monitor Channel", NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel);
        }
    }
}