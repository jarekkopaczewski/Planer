package skills.future.planer.notification;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

public class SerialExecutor implements Executor {
    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private final Executor executor;
    private Runnable active;

    SerialExecutor(Executor executor) {
        this.executor = executor;
    }

    public synchronized void execute(Runnable r) {
        tasks.add(() -> {
            try {
                r.run();
            } finally {
                scheduleNext();
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }

    public void clearQueue() {
        if (active != null)
            synchronized (active) {
                active.notify();
            }
        tasks.clear();
    }
}
