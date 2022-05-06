package skills.future.planer.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import java.util.Calendar;

import skills.future.planer.db.AppDatabase;

public class NotificationFactory {

    private static final String CHANNEL_ID = "100";
    private static final String CHANNEL_NAME = "Habit Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel of Planer application";
    private final AppDatabase appDatabase;
    private LifecycleOwner lifecycleOwner;

    private static int notificationId = 1;
    private final Context context;

    public NotificationFactory(Context context, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        appDatabase = AppDatabase.getInstance(context);
    }

    public void generateNewNotification() {
        appDatabase.habitDao().getHabits().observe(lifecycleOwner,
                habitData -> habitData.forEach(habitData1 -> {
                    createHabitNotificationChannel();
                    new Notification(
                            context,
                            CHANNEL_ID,
                            habitData1,
                            notificationId,
                            Calendar.getInstance().getTimeInMillis()).show();
                    notificationId++;
                }));
        notificationId++;
    }

    private void createHabitNotificationChannel() {
        var channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
