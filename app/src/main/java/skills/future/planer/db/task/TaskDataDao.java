package skills.future.planer.db.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

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
    TaskData findById(Long id) throws Exception;

    /**
     * Method insert given taskData to database
     *
     * @param taskData which will be inserted to database
     * @return new taskId
     */
    @Insert
    long insert(TaskData taskData);

    /**
     * @return all taskData from database
     */
    @Query("SELECT * FROM taskData ORDER BY startingDate, endingDate ASC")
    LiveData<List<TaskData>> getTaskData();

    /**
     * @param taskCategory specified taskCategory
     * @return all taskData with specified taskCategory
     */
    @Query("SELECT * FROM taskData WHERE category = :taskCategory ORDER BY startingDate, endingDate ASC")
    List<TaskData> getTaskData(TaskCategory taskCategory);

    /**
     * @param date specified date
     * @return all taskData with specified taskCategory from specified day
     */
    @Query("SELECT * FROM taskData WHERE priorities =:priorities AND timePriority = :timePriority " +
            "AND :date >= startingDate AND :date <= endingDate")
    LiveData<List<TaskData>> getTaskData(Priorities priorities, TimePriority timePriority, long date);

    /**
     * @param date date in long format
     * @return all taskData with specified date
     */
    @Query("SELECT * FROM taskData WHERE :date >= startingDate AND :date <= endingDate")
    LiveData<List<TaskData>> getTaskDataByDate(long date);

    @Query("SELECT * FROM taskData WHERE status = :status ORDER BY startingDate, endingDate ASC")
    List<TaskData> getTaskData(int status) throws Exception;

    @Query("SELECT * FROM taskData WHERE status = :status ORDER BY startingDate DESC, endingDate DESC")
    List<TaskData> getTaskData_desc(int status) throws Exception;

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
     * Method update taskData in database
     * @param taskData which will be updated
     */
    @Update
    void editOne(TaskData taskData);

    /**
     * @return taskId of lastAdded task
     */
    @Query("SELECT MAX(taskDataId) FROM taskData")
    Long getIdOfLastAddedTask();

    @Query("SELECT COUNT(*) FROM TASKDATA WHERE :date >= startingDate AND :date <= endingDate")
    int getNumberOfTaskByDate(long date);
}
