package skills.future.planer.notification;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;

public class Notification {
    private final Context context;
    private final String channelId;
    private final int notificationId;
    private NotificationCompat.Builder builder;

    public Notification(Context context, String channelId,
                        HabitData habitData, int notificationId, long time) {
        this.context = context;
        this.channelId = channelId;
        this.notificationId = notificationId;
        setNotificationSettings(habitData, time);
    }

    private void setNotificationSettings(HabitData habitData, long time) {
        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.today_icon)
                .setContentTitle("Przypomnenie o nawyku")
                .setWhen(time)
                .setContentText("Czas na realizację twojego nawyku: " + habitData.getTitle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);
    }

    public void show() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
