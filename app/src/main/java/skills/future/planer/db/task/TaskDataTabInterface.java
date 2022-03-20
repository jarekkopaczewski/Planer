package skills.future.planer.db.task;

import android.database.sqlite.SQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public interface TaskDataTabInterface {
    TaskData findById(int id) throws Exception;

    boolean addOne(TaskData taskData);

    List<TaskData> getTaskData() throws Exception;

    List<TaskData> getTaskData(CalendarDay startingDay, CalendarDay calendarDay) throws Exception;

    List<TaskData> getTaskData(TaskCategory taskCategory) throws Exception;

    List<TaskData> getTaskData(Priorities priorities, TimePriority timePriority) throws Exception;

    boolean deleteOne(TaskData taskData);

    boolean editOne(TaskData taskData);
}
