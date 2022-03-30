package skills.future.planer.db.task.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

@Dao
public interface TaskDataTabDao {
    @Query("SELECT * FROM taskData WHERE taskDataId = :id")
    TaskData findById(int id) throws Exception;

    @Insert
    void addOne(TaskData taskData);

    @Query("SELECT * FROM taskData")
    List<TaskData> getTaskData() throws Exception;
//
//    Query("SELECT * FROM ta"
//    List<TaskData> getTaskData(CalendarDay startingDay, CalendarDay calendarDay) throws Exception;
    @Query("SELECT * FROM taskData WHERE category = :taskCategory ")
    List<TaskData> getTaskData(TaskCategory taskCategory) throws Exception;

    @Query("SELECT * FROM taskData WHERE priorities =:priorities AND timePriority = :timePriority")
    List<TaskData> getTaskData(Priorities priorities, TimePriority timePriority) throws Exception;

    @Delete
    void deleteOne(TaskData taskData);

    @Update
    void editOne(TaskData taskData);

    @Query("SELECT MAX(taskDataId) FROM taskData")
    int getIdOfLastAddedTask();
}
