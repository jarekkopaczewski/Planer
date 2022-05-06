package skills.future.planer.notification;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.lifecycle.LifecycleService;

public class NotificationService extends LifecycleService {

    private NotificationFactory notificationFactory;

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }

    private final IBinder binder = new LocalBinder();
    private Thread serviceThread;


    @Override
    public void onCreate() {
        super.onCreate();
        notificationFactory = new NotificationFactory(getApplicationContext(), this);
        serviceThread = new Thread(() -> {

        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        notificationFactory.generateNewNotification(false);
        /*Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
*/
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}


