package skills.future.planer.notification;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.lifecycle.LifecycleService;

import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;
import skills.future.planer.tools.DatesParser;

public class NotificationService extends LifecycleService {


    private NotificationFactory notificationFactory;
    private HabitRepository habitRepository;

    private final IBinder binder = new LocalBinder();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ExecutorService summaryExecutor = Executors.newSingleThreadExecutor();
    private NotificationExecutor notificationExecutor;


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

    @SneakyThrows
    @Override
    public void onCreate() {
        super.onCreate();
        habitRepository = new HabitRepository(getApplication());
        notificationFactory = new NotificationFactory(getApplicationContext(), this, habitRepository);

        notificationExecutor = new NotificationExecutor(executor);

        summaryRunnable();
        habitRunnable();
    }

    private void habitRunnable() {
        habitRepository.getAllHabitDataFromDay(Calendar.getInstance().getTimeInMillis())
                .observe(this, habitDataList -> Executors.newSingleThreadExecutor().execute(() -> {
                    var calendarTime = Calendar.getInstance();
                    var time = calendarTime.getTimeInMillis();

                    calendarTime.set(Calendar.HOUR_OF_DAY, 1);
                    calendarTime.set(Calendar.MINUTE, 0);

                    var deltaTime = time - calendarTime.getTimeInMillis();

                    notificationExecutor.clearQueue();

                    final long[] timeBetween = {0, 0};
                    habitDataList.stream()
                            .filter(habitData -> habitData.isDayOfWeekChecked(DatesParser.toLocalDate(time)))
                            .filter(habitData -> habitData.getNotificationTime() - deltaTime > 0)
                            .sorted(Comparator.comparing(HabitData::getNotificationTime))
                            .peek(habitData -> {
                                if (timeBetween[0] != 0)
                                    timeBetween[1] = timeBetween[0];
                                timeBetween[0] += habitData.getNotificationTime() - deltaTime;
                            })
                            .forEach(habitData -> notificationExecutor
                                    .execute(createNewRunnableNotification(
                                            habitData,
                                            habitData.getNotificationTime() - deltaTime - timeBetween[1])));
                }));
    }

    private void summaryRunnable() {
        summaryExecutor.submit(() -> {
            String[] timeTable = "20:00".split(":");
            var summaryTime = Calendar.getInstance();
            summaryTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeTable[0]));
            summaryTime.set(Calendar.MINUTE, Integer.parseInt(timeTable[1]));
            var time = summaryTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            if (time > 0)
                summaryExecutor.execute(createNewRunnableNotification(time));
        });
    }

    /**
     * Creates habit notification
     */
    private Runnable createNewRunnableNotification(HabitData habitData, long timeToNotification) {
        return () -> {
            synchronized (this) {
                try {
                    wait(timeToNotification);
                    notificationFactory.generateNewNotification(false, habitData);
                    notificationExecutor.scheduleNext();
                } catch (InterruptedException ignored) {
                }
            }
        };
    }

    /**
     * Creates summary notification
     */
    private Runnable createNewRunnableNotification(long timeToNotification) {
        return () -> {
            synchronized (this) {
                try {
                    wait(timeToNotification);
                    notificationFactory.generateNewNotification(true, null);
                    notificationExecutor.scheduleNext();
                } catch (InterruptedException ignored) {
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

}


