package skills.future.planer.notification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import skills.future.planer.R;

public class NotificationService extends Service {

    private final IBinder binder = new LocalBinder();


    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
    }


    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

//        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, LocalServiceActivities.Controller.class), 0);
        // Set the info for the views that show in the notification panel.
       /* Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.today_icon)
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Title")  // the label of the entry
                .setContentText(text)  // the contents of the entry
               // .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build()*/
        ;

        // Send the notification.
        // mNM.notify(NOTIFICATION, notification);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}
