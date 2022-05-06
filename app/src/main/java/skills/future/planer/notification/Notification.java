package skills.future.planer.notification;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

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

    public Notification(Context context, String channelId, int notificationId, boolean moreThanOne) {
        this.context = context;
        this.channelId = channelId;
        this.notificationId = notificationId;
        setNotificationSettings(moreThanOne);
    }

    private void setNotificationSettings(HabitData habitData, long time) {
        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.today_icon)
                .setContentTitle(context.getText(R.string.title_of_notification_singular))
                .setWhen(time)
                .setContentText(context.getText(R.string.time_of_realization_habit) + habitData.getTitle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);
    }

    private void setNotificationSettings(boolean moreThanOne) {
        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.today_icon)
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);
        if (moreThanOne)
            builder.setContentTitle(context.getText(R.string.title_reminder_about_habit_plural))
                    .setContentText(context.getText(R.string.reminder_about_habit_plural));
        else
            builder.setContentTitle(context.getText(R.string.title_reminder_about_habit_singular))
                    .setContentText(context.getText(R.string.reminder_about_habit_singular));
    }

    public void show() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
