package skills.future.planer.db.goal;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Map;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.task.TaskData;

public class GoalRepository {
    private final GoalsDao goalsDao;


    public GoalRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.goalsDao = db.goalsDao();
    }

    /**
     * @return reference to list of all habits
     */
    LiveData<List<GoalData>> getAllGoals() {
        return goalsDao.getGoals();
    }

    LiveData<Map<GoalData, HabitData>> getHabitsFromGoal(Long goalId) {
        return goalsDao.getHabitsFromGoal(goalId);
    }

    LiveData<Map<GoalData, TaskData>> getTasksFromGoal(Long goalId) {
        return goalsDao.getTasksFromGoal(goalId);
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

    GoalData findById(Long goalId) {
        return goalsDao.findById(goalId);
    }
}
