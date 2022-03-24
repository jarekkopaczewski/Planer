package skills.future.planer.db.task.priority;

import lombok.Getter;

@Getter
public enum TimePriority {
    Urgent(0),
    NotUrgent(1);

    private final int timePriorityLvl;

    TimePriority(int timePriorityLvl) {
        this.timePriorityLvl = timePriorityLvl;
    }
}
