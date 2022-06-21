package skills.future.planer.ui.tasklist.filters_chain;

import java.util.List;
import java.util.stream.Collectors;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.Priorities;

public class TaskPriorityFilter extends BaseFilter {
    private final Priorities important;

    public TaskPriorityFilter(Priorities important) {
        this.important = important;
    }

    @Override
    protected List<TaskData> validate(List<TaskData> tasks) {
        return tasks.stream().filter(
                taskData -> taskData.getPriorities().equals(important))
                .collect(Collectors.toList());
    }
}
