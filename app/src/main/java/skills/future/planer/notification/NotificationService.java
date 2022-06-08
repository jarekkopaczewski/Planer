package skills.future.planer.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleService;
import androidx.preference.PreferenceManager;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.settings.SettingsActivity;

public class NotificationService extends LifecycleService {

    public static Boolean serviceRunning = false;

    private static final long oneDayInMilliseconds = 86400000;
    private static final Object mutex = new Object();

    private NotificationFactory notificationFactory;
    private HabitRepository habitRepository;

    private PendingIntent pendingIntent;
    private final IBinder binder = new LocalBinder();
    private SharedPreferences sharedPref;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private AlarmManager alarmManager;


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

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        habitRepository = new HabitRepository(getApplication());
        notificationFactory = new NotificationFactory(getApplicationContext(), habitRepository);

        sharedPref.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        habitChangeObserver();
        summaryListener();
        serviceRunning = true;
    }


    /**
     * Starts process of scheduling notification when habit change
     */
    private void habitChangeObserver() {
        habitRepository.getAllHabitDataFromDay(Calendar.getInstance().getTimeInMillis())
                .observe(this,
                        habitDataList -> Executors
                                .newSingleThreadExecutor()
                                .execute(() -> findWhichNotificationShouldBeGenerated(habitDataList)));
    }

    /**
     * Starts process of scheduling notification when something run it
     */
    private Runnable habitRunnable() {
        return () -> {
            var list = habitRepository.getAllHabitDataFromDayList(Calendar.getInstance().getTimeInMillis());
            findWhichNotificationShouldBeGenerated(list);
        };
    }

    /**
     * Finds the earliest notification for current time
     */
    private void findWhichNotificationShouldBeGenerated(List<HabitData> habitDataList) {
        var calendarTime = Calendar.getInstance();
        var time = calendarTime.getTimeInMillis();

        calendarTime.set(Calendar.HOUR_OF_DAY, 0);
        calendarTime.set(Calendar.MINUTE, 0);
        calendarTime.set(Calendar.SECOND, 0);
        calendarTime.set(Calendar.MILLISECOND, 0);
        var deltaTime = time - calendarTime.getTimeInMillis();

        synchronized (mutex) {
            Optional<HabitData> minHabit = habitDataList.stream()
                    .filter(habitData -> habitData.isDayOfWeekChecked(DatesParser.toLocalDate(time)))
                    .filter(habitData -> habitData.getNotificationTime() - deltaTime > 0)
                    .filter(habitData -> !habitData.isHabitDone(CalendarDay.today())).min(Comparator.comparing(HabitData::getNotificationTime));

            setAlarmManagerOnSpecificDate(calendarTime, time, deltaTime, minHabit, habitDataList.size());
        }
    }

    /**
     * Counts time for habits summary
     */
    private long countTimeToHabitSummary(SharedPreferences sharedPref) {
        int time_str = sharedPref.getInt(SettingsActivity.KEY_PREF_TIME, 72000);
        var summaryTime = Calendar.getInstance();
        var hours = time_str / 3600;
        summaryTime.set(Calendar.HOUR_OF_DAY, hours);
        summaryTime.set(Calendar.MINUTE, (time_str - 3600 * hours) / 60);

        var calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.HOUR_OF_DAY, 0);
        calendarTime.set(Calendar.MINUTE, 0);
        calendarTime.set(Calendar.SECOND, 0);


        return summaryTime.getTimeInMillis() - calendarTime.getTimeInMillis();
    }

    /**
     * Schedules alarm manager which will wake up a broadcast receiver for specific date
     */
    private void setAlarmManager(boolean daySummary, HabitData habitToNotify, long time, LocalDate date) {
        var myNotification = notificationFactory.generateNewNotification(
                daySummary,
                habitToNotify,
                date);

        Intent notificationIntent = new Intent(this, NotificationBroadcastReceiver.class);

        if (myNotification != null) {
            notificationIntent.putExtra(NotificationBroadcastReceiver.NOTIFICATION_ID, myNotification.getNotificationId());
            notificationIntent.putExtra(NotificationBroadcastReceiver.NOTIFICATION, myNotification.getNotification());
        }

        cancelAlarmManager();

        pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + time,
                pendingIntent);
    }

    /**
     * Cancels current alarm manager
     */
    private synchronized void cancelAlarmManager() {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * Checks which timer should be scheduled
     */
    private void setAlarmManagerOnSpecificDate(Calendar calendarTime, long time, long deltaTime,
                                               Optional<HabitData> minHabit, int size) {
        long habitSummaryTime = countTimeToHabitSummary(sharedPref);
        LocalDate currentDate = LocalDate.now();

        if (minHabit.isPresent()) {
            HabitData hd = minHabit.get();
            if (habitSummaryTime - deltaTime < 0 || habitSummaryTime > hd.getNotificationTime())
                setAlarmManager(false, hd, hd.getNotificationTime() - deltaTime, currentDate);
            else
                setAlarmManager(true, null, habitSummaryTime - deltaTime, currentDate);
        } else {
            if (habitSummaryTime - deltaTime > 0)
                setAlarmManager(true, null, habitSummaryTime - deltaTime, currentDate);
            else if (size != 0) {
                calendarTime.set(Calendar.HOUR_OF_DAY, 24);
                calendarTime.set(Calendar.MINUTE, 0);
                calendarTime.set(Calendar.SECOND, 0);
                checkNextDay(currentDate, calendarTime.getTimeInMillis() - time);
            }

        }
    }

    /**
     * Checks is needed to generate notification for next day/days
     */
    private void checkNextDay(@NonNull LocalDate date, long timeToNextDay) {
        LocalDate nextDay = date.plusDays(1);

        var calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.HOUR_OF_DAY, 0);
        calendarTime.set(Calendar.MINUTE, 0);
        calendarTime.set(Calendar.SECOND, 0);

        synchronized (mutex) {
            cancelAlarmManager();

            Optional<HabitData> minHabit = habitRepository.getAllHabitDataFromDayList(DatesParser.toMilliseconds(nextDay))
                    .stream()
                    .filter(habitData -> habitData.isDayOfWeekChecked(nextDay))
                    .filter(habitData -> !habitData.isHabitDone(DatesParser.toCalendarDay(nextDay)))
                    .min(Comparator.comparing(HabitData::getNotificationTime));

            long habitSummaryTime = countTimeToHabitSummary(sharedPref);
            if (minHabit.isPresent()) {
                HabitData hd = minHabit.get();
                if (hd.getNotificationTime() < habitSummaryTime)
                    setAlarmManager(false, hd, timeToNextDay + hd.getNotificationTime(), nextDay);
                else
                    setAlarmManager(true, null, timeToNextDay + habitSummaryTime, nextDay);
            } else
                checkNextDay(nextDay, timeToNextDay + oneDayInMilliseconds);

        }
    }

    /**
     * Recreates the summary notification after edit summary time in settings
     */
    private void summaryListener() {
        preferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(SettingsActivity.KEY_PREF_TIME))
                Executors.newSingleThreadExecutor().execute(habitRunnable());
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        serviceRunning = true;
        Executors.newSingleThreadExecutor().execute(habitRunnable());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceRunning = false;
    }
}


