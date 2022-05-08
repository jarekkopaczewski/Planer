package skills.future.planer.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;

public class NotificationFactory {

    private static final String CHANNEL_ID = "100";
    private static final String CHANNEL_NAME = "Habit Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel of Planer application";
    private final AppDatabase appDatabase;
    private HabitRepository habitRepository;
    private HabitData habitNotify;
    private int numberOfNotDoneHabits;
    private LifecycleOwner lifecycleOwner;

    private static int notificationId = 1;
    private final Context context;

    public NotificationFactory(Context context, LifecycleOwner lifecycleOwner, HabitRepository habitRepository) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        appDatabase = AppDatabase.getInstance(context);
        this.habitRepository = habitRepository;
        createDatabaseObservers();
    }

    private void createDatabaseObservers() {

        habitRepository.getAllHabitDataFromDay(Calendar.getInstance().getTimeInMillis())
                .observe(lifecycleOwner, habitDataList -> {
                    numberOfNotDoneHabits = 0;
                    habitDataList.forEach(habitData -> {
                        if (!habitData.isHabitDone(CalendarDay.today()))
                            numberOfNotDoneHabits++;
                    });
                });
    }


    /**
     * Generates Notification depending on type
     */
    public void generateNewNotification(boolean daySummary, HabitData habitNotify) {
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
                    notificationId).show();

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
