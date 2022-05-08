package skills.future.planer.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.lifecycle.LifecycleService;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.settings.SettingsActivity;

public class NotificationService extends LifecycleService {

    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private SharedPreferences sharedPref;
    private NotificationFactory notificationFactory;
    private HabitRepository habitRepository;

    private final IBinder binder = new LocalBinder();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private Thread serviceThread;
    private Long sleepTime = null;
    private boolean clearNotification;

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

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SerialExecutor serialExecutor = new SerialExecutor(executor);


       /* createNewThread();
        executor.execute(()->{
            while (true) {
                try {
                    String time = sharedPref.getString(SettingsActivity.KEY_PREF_TIME, "20:00");
                    String [] timeTable =  time.split(":");
                    Arrays.stream(timeTable).forEach(System.out::println);
                    var calendarTime = Calendar.getInstance();
                    calendarTime.set(Calendar.HOUR_OF_DAY, 1);
                    calendarTime.set(Calendar.MINUTE, 0);
                    var deltaTime = Calendar.getInstance().getTimeInMillis() - calendarTime.getTimeInMillis();
                    sleepTime = habitRepository.getNextNotification(deltaTime);
                    if (sleepTime != null) {
                        synchronized (executor) {
                            executor.wait(sleepTime);
                        }
                        notificationFactory.generateNewNotification(
                                false,
                                sleepTime,
                                habitRepository.getNextNotificationHabit(deltaTime));

                    } else
                        synchronized (executor) {
                            executor.wait();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/


        habitRepository.getAllHabitDataFromDay(Calendar.getInstance().getTimeInMillis())
                .observe(this, habitDataList -> new Thread(() -> {
                    var calendarTime = Calendar.getInstance();
                    var time = calendarTime.getTimeInMillis();

                    calendarTime.set(Calendar.HOUR_OF_DAY, 1);
                    calendarTime.set(Calendar.MINUTE, 0);

                    var deltaTime = time - calendarTime.getTimeInMillis();

                    String[] timeTable = sharedPref.getString(SettingsActivity.KEY_PREF_TIME, "20:00").split(":");

                    calendarTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeTable[0]));
                    calendarTime.set(Calendar.MINUTE, Integer.parseInt(timeTable[1]));

                    clearNotification = true;
                    serialExecutor.clearQueue();
                    clearNotification = false;

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
                                serialExecutor
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!clearNotification)
                    notificationFactory.generateNewNotification(false, habitData);

            }
        };
    }

    private Runnable createNewRunnableNotification(long timeToNotification) {
        return () -> {
            synchronized (this) {
                try {
                    wait(timeToNotification);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!clearNotification)
                notificationFactory.generateNewNotification(true, null);
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        synchronized (habitRepository) {
            habitRepository.notify();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}


