package skills.future.planer.db.task;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import skills.future.planer.db.task.category.TaskCategory;
import skills.future.planer.db.task.priority.Priorities;
import skills.future.planer.db.task.priority.TimePriority;

@Getter
@Setter
public class TaskData implements Serializable {
    private int taskDataId;
    private Boolean status;
    private TaskCategory category;
    private Priorities priorities;
    private TimePriority timePriority;
    private String taskTitleText, taskDetailsText;
    private CalendarDay startingDate = null;
    private CalendarDay endingDate = null;

    public TaskData(int taskDataId, Boolean status, int category, int priorities,
                    int timePriority, String taskTitleText,String taskDetailsText,
                    CalendarDay startingDate, CalendarDay endingDate) {
        this.taskDataId = taskDataId;
        this.status = status;
        switch (category) {
            case 0 -> this.category = TaskCategory.Private;
            case 1 -> this.category = TaskCategory.Work;
        }
        switch (priorities) {
            case 0 -> this.priorities = Priorities.Important;
            case 1 -> this.priorities = Priorities.NotImportant;
        }
        switch (timePriority) {
            case 0 -> this.timePriority = TimePriority.Urgent;
            case 1 -> this.timePriority = TimePriority.NotUrgent;
        }
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
    }

    public TaskData(TaskCategory category, Priorities priorities, TimePriority timePriority,
                    String taskTitleText, String taskDetailsText) {
        this.taskDataId = 0;
        this.status = false;
        this.category = category;
        this.priorities = priorities;
        this.timePriority = timePriority;
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
    }

    public TaskData(TaskCategory category, Priorities priorities, TimePriority timePriority,
                    String taskTitleText, String taskDetailsText, CalendarDay startingDate, CalendarDay endingDate) {
        this.category = category;
        this.priorities = priorities;
        this.timePriority = timePriority;
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
    }
}
