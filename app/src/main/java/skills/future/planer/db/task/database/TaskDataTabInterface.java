package skills.future.planer.db.task.database;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.category.TaskCategory;
import skills.future.planer.db.task.priority.Priorities;
import skills.future.planer.db.task.priority.TimePriority;

public interface TaskDataTabInterface {
    TaskData findById(int id) throws Exception;

    boolean addOne(TaskData taskData);

    List<TaskData> getTaskData() throws Exception;

    List<TaskData> getTaskData(CalendarDay startingDay, CalendarDay calendarDay) throws Exception;

    List<TaskData> getTaskData(TaskCategory taskCategory) throws Exception;

    List<TaskData> getTaskData(Priorities priorities, TimePriority timePriority) throws Exception;

    boolean deleteOne(TaskData taskData);

    boolean editOne(TaskData taskData);

    int getIdOfLastAddedTask();
}
