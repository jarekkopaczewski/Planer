package skills.future.planer.db.goal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalsDao {
    @Transaction
    @Query("SELECT * FROM GoalData")
    public abstract LiveData<List<GoalsHabitRelation>> getGoalsWithHabits();

    /**
     * Method get GoalData with given id
     *
     * @param id of searched habitData
     * @return GoalData
     * @throws Exception if sth is wrong xd
     */
    @Query("SELECT * FROM GoalData WHERE goalId = :id")
    GoalData findById(Long id) throws Exception;

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
