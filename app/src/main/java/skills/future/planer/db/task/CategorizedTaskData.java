package skills.future.planer.db.task;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class CategorizedTaskData {
     List<TaskData> importantUrgentTask, importantNotUrgent,
            notImportantUrgentTask, notImportantNotUrgent;

    public CategorizedTaskData() {
        importantUrgentTask = new ArrayList<>();
        importantNotUrgent = new ArrayList<>();
        notImportantUrgentTask = new ArrayList<>();
        notImportantNotUrgent = new ArrayList<>();
    }
}