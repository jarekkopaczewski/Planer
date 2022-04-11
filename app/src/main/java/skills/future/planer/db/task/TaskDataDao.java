package skills.future.planer.db.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;
import java.util.Locale;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

@Dao
public interface TaskDataDao {
    /**
     * Method get taskData with given id
     *
     * @param id of searched taskData
     * @return taskData
     * @throws Exception if sth is wrong xd
     */
    @Query("SELECT * FROM taskData WHERE taskDataId = :id")
    TaskData findById(int id) throws Exception;

    /**
     * Method insert given taskData to database
     *
     * @param taskData which will be inserted to database
     */
    @Insert
    void insert(TaskData taskData);

    /**
     * @return all taskData from database
     */
    @Query("SELECT * FROM taskData")
    LiveData<List<TaskData>> getTaskData();

    /**
     * @param taskCategory specified taskCategory
     * @return all taskData with specified taskCategory
     * @throws Exception
     */
    @Query("SELECT * FROM taskData WHERE category = :taskCategory ")
    List<TaskData> getTaskData(TaskCategory taskCategory) throws Exception;

    /**
     * @param priorities   specified taskCategory
     * @param timePriority specified timePriority
     * @return all taskData with specified priorities and timePriority
     * @throws Exception
     */
    @Query("SELECT * FROM taskData WHERE priorities =:priorities AND timePriority = :timePriority")
   List<TaskData> getTaskData(Priorities priorities, TimePriority timePriority) throws Exception;

    @Query("SELECT * FROM taskData WHERE priorities =:priorities AND category = :taskCategory")
    List<TaskData> getTaskData(Priorities priorities, TaskCategory taskCategory) throws Exception;

    @Query("SELECT * FROM taskData WHERE timePriority =:timePriority AND category = :taskCategory")
    List<TaskData> getTaskData(TaskCategory taskCategory, TimePriority timePriority) throws Exception;

    @Query("SELECT * FROM taskData WHERE priorities =:priorities AND timePriority = :timePriority AND category = :category")
    List<TaskData> getTaskData(Priorities priorities, TimePriority timePriority, TaskCategory category) throws Exception;

    @Query("SELECT * FROM taskData WHERE timePriority = :timePriority ")
    List<TaskData> getTaskData(TimePriority timePriority) throws Exception;

    @Query("SELECT * FROM taskData WHERE priorities = :priorities ")
    List<TaskData> getTaskData(Priorities priorities) throws Exception;

    /**
     * Method delete taskData from database
     *
     * @param taskData which will be deleted
     */
    @Delete
    void deleteOne(TaskData taskData);

    /**
     * Method delete all taskData from database
     */
    @Query("DELETE FROM taskData")
    void deleteAll();

    /**
     * @param taskData
     */
    @Update
    void editOne(TaskData taskData);

    /**
     * @return
     */
    @Query("SELECT MAX(taskDataId) FROM taskData")
    int getIdOfLastAddedTask();
}
