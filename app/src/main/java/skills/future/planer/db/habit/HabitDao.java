package skills.future.planer.db.habit;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {
    /**
     * @return all habits from database
     */
    @Query("SELECT * FROM HabitData")
    LiveData<List<HabitData>> getHabits();

    /**
     * @param date date in long format
     * @return all habits with specified date
     */
    @Query("SELECT * FROM HabitData WHERE :date >= beginDay AND :date <= endDay")
    LiveData<List<HabitData>> getTaskDataByDate(long date);

    /**
     * Method insert given habit to database
     *
     * @param habitData which will be inserted to database
     * @return new habit id
     */
    @Insert
    long insert(HabitData habitData);

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
