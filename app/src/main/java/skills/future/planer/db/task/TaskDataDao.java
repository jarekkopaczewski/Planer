package skills.future.planer.db.task;

import androidx.lifecycle.LiveData;
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
public interface TaskDataDao {
    @Query("SELECT * FROM taskData WHERE taskDataId = :id")
    TaskData findById(int id) throws Exception;

    @Insert
    void insert(TaskData taskData);

    @Query("SELECT * FROM taskData")
    LiveData<List<TaskData>> getTaskData();

    @Query("SELECT * FROM taskData WHERE category = :taskCategory ")
    List<TaskData> getTaskData(TaskCategory taskCategory) throws Exception;

    @Query("SELECT * FROM taskData WHERE priorities =:priorities AND timePriority = :timePriority")
    List<TaskData> getTaskData(Priorities priorities, TimePriority timePriority) throws Exception;

    @Delete
    void deleteOne(TaskData taskData);

    @Query("DELETE FROM taskData")
    void deleteAll();

    @Update
    void editOne(TaskData taskData);

    @Query("SELECT MAX(taskDataId) FROM taskData")
    int getIdOfLastAddedTask();
}
