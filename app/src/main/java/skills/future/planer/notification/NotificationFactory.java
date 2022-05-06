package skills.future.planer.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import java.util.Calendar;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.habit.HabitData;

public class NotificationFactory {

    private static final String CHANNEL_ID = "100";
    private static final String CHANNEL_NAME = "Habit Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel of Planer application";
    private final AppDatabase appDatabase;
    private HabitData habitNotify;
    private int numberOfNotDoneHabits;
    private LifecycleOwner lifecycleOwner;

    private static int notificationId = 1;
    private final Context context;

    public NotificationFactory(Context context, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        appDatabase = AppDatabase.getInstance(context);
        createDatabaseObservers();
    }

    private void createDatabaseObservers() {
        appDatabase.habitDao().getHabits().observe(lifecycleOwner,
                habitData -> habitData.forEach(habitData1 -> habitNotify = habitData1));
        appDatabase.habitDao().getHabits().observe(lifecycleOwner,
                habitData -> numberOfNotDoneHabits = habitData.size());
    }


    /**
     * Generates Notification depending on type
     */
    public void generateNewNotification(boolean daySummary) {
        createHabitNotificationChannel();
        if (daySummary) {
            if (numberOfNotDoneHabits != 0)
                new Notification(
                        context,
                        CHANNEL_ID,
                        notificationId,
                        numberOfNotDoneHabits > 1);
        } else
            new Notification(
                    context,
                    CHANNEL_ID,
                    habitNotify,
                    notificationId,
                    Calendar.getInstance().getTimeInMillis()).show();

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
