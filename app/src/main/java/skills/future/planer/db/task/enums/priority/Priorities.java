package skills.future.planer.db.task.enums.priority;

import lombok.Getter;

@Getter
public enum Priorities {
    Important(0),
    NotImportant(1);

    private final int priorityLvl;

    Priorities(int priorityLvl) {
        this.priorityLvl = priorityLvl;
    }

}
