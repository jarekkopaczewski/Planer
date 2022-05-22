package skills.future.planer.db.goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.task.TaskData;

@Dao
public interface GoalsDao {
    @Query("SELECT * FROM GoalData JOIN HabitData ON GoalData.goalId = HabitData.foreignKeyToGoal  WHERE GoalData.goalId = :goalId")
    LiveData<Map<GoalData, HabitData>> getHabitsFromGoal(Long goalId);

    @Transaction
    @Query("SELECT * FROM GoalData JOIN taskData ON GoalData.goalId = taskData.foreignKeyToGoal WHERE GoalData.goalId = :goalId ")
    LiveData<Map<GoalData, TaskData>> getTasksFromGoal(Long goalId);

    @Query("SELECT * FROM GoalData JOIN HabitData ON GoalData.goalId = HabitData.foreignKeyToGoal  WHERE GoalData.goalId = :goalId")
    Map<GoalData, HabitData> getHabitsFromGoalWithoutLiveData(Long goalId);

    @Transaction
    @Query("SELECT * FROM GoalData JOIN taskData ON GoalData.goalId = taskData.foreignKeyToGoal WHERE GoalData.goalId = :goalId ")
    Map<GoalData, TaskData> getTasksFromGoalWithoutLiveData(Long goalId);

    /**
     * Method get GoalData with given id
     *
     * @param id of searched habitData
     * @return GoalData
     */
    @Query("SELECT * FROM GoalData WHERE goalId = :id")
    GoalData findById(Long id);

    /**
     * @return all goals from database
     */
    @Query("SELECT * FROM GoalData")
    LiveData<List<GoalData>> getGoals();

    /**
     * Method insert given goals to database
     *
     * @param goalData which will be inserted to database
     * @return new goalData id
     */
    @Insert
    long insert(GoalData goalData);

    /**
     * Method delete goalData from database
     *
     * @param goalData which will be deleted
     */
    @Delete
    void deleteOne(GoalData goalData);

    /**
     * Method delete all habits from database
     */
    @Query("DELETE FROM GoalData")
    void deleteAll();

    /**
     * Method update given habit
     *
     * @param goalData given habit which will be updated
     */
    @Update
    void editOne(GoalData goalData);
}
