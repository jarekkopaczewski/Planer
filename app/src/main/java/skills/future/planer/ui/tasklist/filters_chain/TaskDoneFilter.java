package skills.future.planer.ui.tasklist.filters_chain;

import java.util.List;
import java.util.stream.Collectors;

import skills.future.planer.db.task.TaskData;

public class TaskDoneFilter extends BaseFilter {
    private final boolean filterStatus;

    public TaskDoneFilter(boolean b) {
        super();
        filterStatus = b;
    }

    @Override
    protected List<TaskData> validate(List<TaskData> tasks) {
        return filterStatus ?
                tasks.stream().filter(TaskData::getStatus).collect(Collectors.toList()) :
                tasks.stream().filter(taskData -> !taskData.getStatus()).collect(Collectors.toList());
    }
}

