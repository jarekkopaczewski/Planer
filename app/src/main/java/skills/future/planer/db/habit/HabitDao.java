package skills.future.planer.db.habit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import skills.future.planer.db.task.TaskData;

@Dao
public interface HabitDao {
    /**
     * Method insert given habit to database
     *
     * @param habitData which will be inserted to database
     */
    @Insert
    void insert(HabitData habitData);

    /**
     * Method delete habit from database
     *
     * @param habitData which will be deleted
     */
    @Delete
    void deleteOne(HabitData habitData);

    /**
     * Method delete all habits from database
     */
    @Query("DELETE FROM HabitData")
    void deleteAll();

    /**
     * Method update given habit
     * @param habitData given habit which will be updated
     */
    @Update
    void editOne(HabitData habitData);
}
