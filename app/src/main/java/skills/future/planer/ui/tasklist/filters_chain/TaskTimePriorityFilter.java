package skills.future.planer.ui.tasklist.filters_chain;

import java.util.List;
import java.util.stream.Collectors;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.TimePriority;

public class TaskTimePriorityFilter extends BaseFilter {
    private final TimePriority urgent;

    public TaskTimePriorityFilter(TimePriority urgent) {
        this.urgent = urgent;
    }

    @Override
    protected List<TaskData> validate(List<TaskData> tasks) {
        return tasks.stream().filter(taskData -> taskData.getTimePriority().equals(urgent)).collect(Collectors.toList());
    }
}
