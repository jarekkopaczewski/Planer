package skills.future.planer.db.goal;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import skills.future.planer.db.AppDatabase;

public class GoalRepository {
    private final GoalsDao goalsDao;
    private LiveData<List<GoalData>> goals;
    private LiveData<List<GoalsHabitRelation>> goalsHabit;

    public GoalRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.goalsDao = db.goalsDao();
    }

    /**
     * @return reference to list of all habits
     */
    LiveData<List<GoalData>> getAllGoals() {
        if (goals == null)
            goals = goalsDao.getGoals();
        return goals;
    }

    LiveData<List<GoalsHabitRelation>> getGoalsWithHabits() {
        if (goalsHabit == null)
            goalsHabit = goalsDao.getGoalsWithHabits();
        return goalsHabit;
    }

    /**
     * Method insert habit to database
     *
     * @param goalData which will be inserted
     */
    void insert(GoalData goalData) {
        goalData.setGoalId(goalsDao.insert(goalData));
    }

    /**
     * Method edit habit in database
     *
     * @param goalData which will be updated
     */
    void edit(GoalData goalData) {
        goalsDao.editOne(goalData);
    }

    /**
     * Method delete habit from database
     *
     * @param goalData which will be deleted
     */
    void delete(GoalData goalData) {
        goalsDao.deleteOne(goalData);
    }

    GoalData findById(Long goalId) throws Exception {
        return goalsDao.findById(goalId);
    }
}