package skills.future.planer.notification;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.lifecycle.LifecycleService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.settings.SettingsActivity;

public class NotificationService extends LifecycleService {


    private NotificationFactory notificationFactory;
    private HabitRepository habitRepository;

    private final IBinder binder = new LocalBinder();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
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


        habitRepository.getAllHabitDataFromDay(Calendar.getInstance().getTimeInMillis())
                .observe(this, habitDataList -> new Thread(() -> {
                    var calendarTime = Calendar.getInstance();
                    var time = calendarTime.getTimeInMillis();

                    calendarTime.set(Calendar.HOUR_OF_DAY, 1);
                    calendarTime.set(Calendar.MINUTE, 0);

                    var deltaTime = time - calendarTime.getTimeInMillis();

                    String[] timeTable = SettingsActivity.chosenTimeField.split(":");
                    Arrays.stream(timeTable).forEach(System.out::println);

                    calendarTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeTable[0]));
                    calendarTime.set(Calendar.MINUTE, Integer.parseInt(timeTable[1]));


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
                            .forEach(habitData -> {
                                var test = habitData.getNotificationTime() - deltaTime - timeBetween[1];
                                System.out.println(test);
                                notificationExecutor
                                        .execute(createNewRunnableNotification(
                                                habitData, test));
                            });
                }).start());
    }

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
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}


