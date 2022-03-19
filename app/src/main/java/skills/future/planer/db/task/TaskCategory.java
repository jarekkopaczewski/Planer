package skills.future.planer.db.task;

import lombok.Getter;

@Getter
public enum TaskCategory {
    Private(0),
    Work(1);

    private final int taskCategory;

    TaskCategory(int typeOfTask) {
        this.taskCategory = typeOfTask;
    }
}
