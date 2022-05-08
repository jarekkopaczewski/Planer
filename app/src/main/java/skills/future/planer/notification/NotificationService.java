package skills.future.planer.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import androidx.lifecycle.LifecycleService;
import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.settings.SettingsActivity;

public class NotificationService extends LifecycleService {


    private NotificationFactory notificationFactory;
    private HabitRepository habitRepository;

    private final IBinder binder = new LocalBinder();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ExecutorService summaryExecutor = Executors.newSingleThreadExecutor();
    private NotificationExecutor notificationExecutor;
    private SharedPreferences sharedPref;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

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


    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        habitRepository = new HabitRepository(getApplication());
        notificationFactory = new NotificationFactory(getApplicationContext(), this, habitRepository);
        notificationExecutor = new NotificationExecutor(executor);

        summaryRunnable();
        sharedPref.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

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
        preferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(SettingsActivity.KEY_PREF_TIME)) {
                summaryExecutor.execute(() -> {
                    int time_str = sharedPreferences.getInt(SettingsActivity.KEY_PREF_TIME, 72000);
                    var summaryTime = Calendar.getInstance();
                    var hours = time_str / 3600;
                    summaryTime.set(Calendar.HOUR_OF_DAY, hours);
                    summaryTime.set(Calendar.MINUTE, (time_str - 3600 * hours) / 60);
                    var time = summaryTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                    if (time > 0)
                        summaryExecutor.execute(createNewRunnableNotification(time));
                });
            }
        };
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


