package skills.future.planer.ui.tasklist.filters_chain;

import java.util.List;
import java.util.stream.Collectors;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.category.TaskCategory;

public class TaskCategoryFilter extends BaseFilter {
    private final TaskCategory work;

    public TaskCategoryFilter(TaskCategory work) {
        this.work = work;
    }

    @Override
    protected List<TaskData> validate(List<TaskData> tasks) {
        return tasks.stream().filter(taskData -> taskData.getCategory().equals(work)).collect(Collectors.toList());
    }
}
