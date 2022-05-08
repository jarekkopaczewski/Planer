package skills.future.planer.notification;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class NotificationExecutor implements Executor {
    private final Queue<Runnable> habits = new ArrayDeque<>();
    private Future<?> future;
    private final ExecutorService executor;
    private Runnable active;

    NotificationExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public synchronized void execute(Runnable r) {
        habits.add(r);
        if (active == null) {
            scheduleNext();
        }
    }

    public synchronized void scheduleNext() {
        if ((active = habits.poll()) != null) {
            future = executor.submit(active);
        }
    }

    /**
     * Clears queue and interrupt current thread
     */
    public void clearQueue() {
        if (active != null)
            synchronized (future) {
                future.cancel(true);
                active = null;
            }
        habits.clear();
    }
}
