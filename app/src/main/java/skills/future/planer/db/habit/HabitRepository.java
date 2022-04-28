package skills.future.planer.db.habit;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.TaskData;

public class HabitRepository {
    private final HabitDao habitDao;
    private LiveData<List<HabitData>> liveListHabits;

    HabitRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.habitDao = db.habitDao();
    }

    /**
     * @return reference to list of all habits
     */
    LiveData<List<HabitData>> getAllHabits() {
        if (liveListHabits == null)
            liveListHabits = habitDao.getHabits();
        return liveListHabits;
    }

    /**
     * Method insert habit to database
     * @param habitData which will be inserted
     */
    void insert(HabitData habitData){
        habitData.setHabitId(habitDao.insert(habitData));
    }

    /**
     * Method edit habit in database
     * @param habitData which will be updated
     */
    void edit(HabitData habitData){
        habitDao.editOne(habitData);
    }

    /**
     * Method delete habit from database
     * @param habitData which will be deleted
     */
    void delete(HabitData habitData){
        habitDao.deleteOne(habitData);
    }
}
