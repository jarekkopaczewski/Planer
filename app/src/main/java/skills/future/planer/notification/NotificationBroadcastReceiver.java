package skills.future.planer.notification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION = "notification";

    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onReceive(Context context, Intent intent) {
        final PendingResult pendingResult = goAsync();
        backgroundExecutor.execute(() -> {
            try {
                Log.println(Log.ASSERT, "test", "Jestem w broadcascie");
                try {
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    Notification notification = intent.getParcelableExtra(NOTIFICATION);
                    notificationManager.notify(intent.getIntExtra(NOTIFICATION_ID, 0), notification);
                } catch (NullPointerException ignore) {
                }

                Intent serviceIntent = new Intent(context.getApplicationContext(), NotificationService.class);
                context.getApplicationContext().startService(serviceIntent);
            } finally {
                pendingResult.finish();
            }
        });
    }
}
