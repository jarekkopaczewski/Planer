package skills.future.planer.ui.summary.generator;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class SummaryService extends Service {

    private final IBinder binder = new SummaryService.LocalBinder();
    private final DayChangeBroadcastReceiver dayChangeBroadcastReceiver = new DayChangeBroadcastReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        registerReceiver(dayChangeBroadcastReceiver, filter);
    }

    public class LocalBinder extends Binder {
        public SummaryService getService() {
            return SummaryService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        registerReceiver(dayChangeBroadcastReceiver, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dayChangeBroadcastReceiver);
    }
}