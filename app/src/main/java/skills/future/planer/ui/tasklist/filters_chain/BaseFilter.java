package skills.future.planer.ui.tasklist.filters_chain;

import java.util.List;

import skills.future.planer.db.task.TaskData;

public abstract class BaseFilter {
    private BaseFilter next;

    public void setNext(BaseFilter handler) {
        if (next != null)
            next.setNext(handler);
        else
            next = handler;
    }

    public List<TaskData> filter(List<TaskData> tasks) {
        if (next != null)
            return validate(next.validate(tasks));
        else
            return validate(tasks);
    }

    protected abstract List<TaskData> validate(List<TaskData> tasks);
}
//todo połączyć kazdy filter z chipami
//todo po odznczeniu chipa odlaczyc filter z lancucha