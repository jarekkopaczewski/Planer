package skills.future.planer.db.task;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

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
        switch (category){
            case 0:
                this.category = TaskCategory.Private;
                break;
            case 1:
                this.category = TaskCategory.Work;
                break;
        }
        switch (priorities){
            case 0:
                this.priorities = Priorities.Important;
                break;
            case 1:
                this.priorities = Priorities.NotImportant;
                break;
        }
        switch (timePriority){
            case 0:
                this.timePriority = TimePriority.Urgent;
                break;
            case 1:
                this.timePriority = TimePriority.NotUrgent;
                break;
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
