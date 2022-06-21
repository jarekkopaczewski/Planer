package skills.future.planer.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import java.time.LocalDate;

import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.tools.DatesParser;

public class NotificationFactory {

    private static final String CHANNEL_ID = "100";
    private static final String CHANNEL_NAME = "Habit Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel of Planer application";

    private static int notificationId = 1;
    private final Context context;
    private HabitRepository habitRepository;

    public NotificationFactory(Context context, HabitRepository habitRepository) {
        this.context = context;
        this.habitRepository = habitRepository;
    }

    public NotificationFactory(Context context) {
        this.context = context;
    }

    /**
     * Counts not done habits for date
     *
     * @return - number of not done habits
     */
    private long countNotDoneNotification(LocalDate date) {
        return habitRepository.getAllHabitDataFromDayList(DatesParser.toMilliseconds(date))
                .stream()
                .filter(habitData -> !habitData.isHabitDone(DatesParser.toCalendarDay(date)))
                .count();
    }

    /**
     * Generates Notification depending on type
     */
    public MyNotification generateNewNotificationHabit(boolean daySummary, HabitData habitNotify, LocalDate date) {
        createHabitNotificationChannel();
        if (daySummary) {
            long numberOfNotDoneHabits = countNotDoneNotification(date);
            if (numberOfNotDoneHabits != 0)
                return new MyNotification(
                        context,
                        CHANNEL_ID,
                        notificationId++,
                        numberOfNotDoneHabits > 1);
        } else
            return new MyNotification(
                    context,
                    CHANNEL_ID,
                    habitNotify,
                    notificationId++);

        return null;
    }

    /**
     * Generates Notification for summary
     */
    public MyNotification generateNewNotificationSummary(SummaryType weekSummary) {
        createHabitNotificationChannel();
        return new MyNotification(context, CHANNEL_ID, notificationId++, weekSummary);
    }

    /**
     * Creates notification channel
     */
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
