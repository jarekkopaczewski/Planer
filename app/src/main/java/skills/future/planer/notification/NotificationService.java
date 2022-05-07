package skills.future.planer.notification;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.lifecycle.LifecycleService;

import java.util.Calendar;

import skills.future.planer.db.habit.HabitRepository;

public class NotificationService extends LifecycleService {

    private NotificationFactory notificationFactory;
    private HabitRepository habitRepository;

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

    private final IBinder binder = new LocalBinder();
    private Thread serviceThread;
    private Long sleepTime = null;

    @Override
    public void onCreate() {
        super.onCreate();
        habitRepository = new HabitRepository(getApplication());
        notificationFactory = new NotificationFactory(getApplicationContext(), this, habitRepository);
        habitRepository.getAllHabits().observe(this, habitData -> {
            if (serviceThread.getState() == Thread.State.TIMED_WAITING)
                serviceThread.notify();
        });


        serviceThread = new Thread(() -> {
            while (true) {
                try {
                    sleepTime = habitRepository.getNextNotification(Calendar.getInstance().getTimeInMillis());
                    if (sleepTime != null) {
                        notificationFactory.generateNewNotification(false, sleepTime);
                        serviceThread.wait(sleepTime);
                    } else
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        if (!serviceThread.isAlive())
            serviceThread.start();


        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}


