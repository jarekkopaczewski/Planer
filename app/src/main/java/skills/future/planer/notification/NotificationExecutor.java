package skills.future.planer.notification;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class NotificationExecutor implements Executor {
    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private Future<?> future;
    private final ExecutorService executor;
    private Runnable active;

    NotificationExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public synchronized void execute(Runnable r) {
        tasks.add(r);
        if (active == null) {
            scheduleNext();
        }
    }

    public synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            future = executor.submit(active);
        }
    }

    public void clearQueue() {
        if (active != null)
            synchronized (future) {
                future.cancel(true);
                active = null;
            }
        tasks.clear();

    }
}
