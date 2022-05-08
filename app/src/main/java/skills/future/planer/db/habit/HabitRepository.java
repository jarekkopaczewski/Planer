package skills.future.planer.db.habit;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import skills.future.planer.db.AppDatabase;

public class HabitRepository {
    private final HabitDao habitDao;
    private LiveData<List<HabitData>> liveListHabits;

    public HabitRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.habitDao = db.habitDao();
    }

    /**
     * @return reference to list of all habits
     */
    public LiveData<List<HabitData>> getAllHabits() {
        if (liveListHabits == null)
            liveListHabits = habitDao.getHabits();
        return liveListHabits;
    }

    /**
     * @return references to the habit list based on day
     */
    public LiveData<List<HabitData>> getAllHabitDataFromDay(long date) {
        return habitDao.getHabitDataByDate(date);
    }

    /**
     * Method insert habit to database
     *
     * @param habitData which will be inserted
     */
    public void insert(HabitData habitData) {
        habitData.setHabitId(habitDao.insert(habitData));
    }

    /**
     * Method edit habit in database
     *
     * @param habitData which will be updated
     */
    public void edit(HabitData habitData) {
        habitDao.editOne(habitData);
    }

    /**
     * Method delete habit from database
     *
     * @param habitData which will be deleted
     */
    public void delete(HabitData habitData) {
        habitDao.deleteOne(habitData);
    }

    HabitData findById(Long habitId) throws Exception {
        return habitDao.findById(habitId);
    }
}
