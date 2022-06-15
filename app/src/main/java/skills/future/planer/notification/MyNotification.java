package skills.future.planer.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import skills.future.planer.MainActivity;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.summary.SummaryType;

public class MyNotification extends android.app.Notification {
    private final Context context;
    private final String channelId;

    public int getNotificationId() {
        return notificationId;
    }

    private final int notificationId;
    private NotificationCompat.Builder builder;
    private PendingIntent pendingIntent;

    public MyNotification(Context context, String channelId,
                          HabitData habitData, int notificationId) {
        this.context = context;
        this.channelId = channelId;
        this.notificationId = notificationId;
        createNotificationIntent();
        setNotificationSettings(habitData);
    }

    public MyNotification(Context context, String channelId, int notificationId, boolean moreThanOne) {
        this.context = context;
        this.channelId = channelId;
        this.notificationId = notificationId;
        createNotificationIntent();
        setNotificationSettings(moreThanOne);
    }

    public MyNotification(Context context, String channelId, int notificationId, SummaryType summaryType) {
        this.context = context;
        this.channelId = channelId;
        this.notificationId = notificationId;
        setNotificationSettings(summaryType);
    }

    private void createNotificationIntent() {
        Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("move", true);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setNotificationSettings(HabitData habitData) {
        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.today_icon)
                .setContentTitle(context.getText(R.string.title_of_notification_singular))
                .setContentText(context.getText(R.string.time_of_realization_habit) + " " + habitData.getTitle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    private void setNotificationSettings(boolean moreThanOne) {
        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.today_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        if (moreThanOne)
            builder.setContentTitle(context.getText(R.string.title_reminder_about_habit_plural))
                    .setContentText(context.getText(R.string.reminder_about_habit_plural))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(context.getText(R.string.reminder_about_habit_plural)));

        else
            builder.setContentTitle(context.getText(R.string.title_reminder_about_habit_singular))
                    .setContentText(context.getText(R.string.reminder_about_habit_singular))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(context.getText(R.string.reminder_about_habit_singular)));
    }

    private void setNotificationSettings(SummaryType summaryType) {
        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.prior)
                .setContentTitle("Podsumowania")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true);
        if (summaryType == SummaryType.monthSummary)
            builder.setContentText("Posiadasz nowe podsumowanie miesiąca do uzupełnienia");
        else
            builder.setContentText("Posiadasz nowe podsumowanie tygodnia do uzupełnienia");
    }

    public Notification getNotification() {
        return builder.build();
    }
}
