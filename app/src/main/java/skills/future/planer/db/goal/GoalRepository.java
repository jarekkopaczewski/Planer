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
    private LiveData<List<GoalData>> goals;
    private LiveData<Map<GoalData, List<HabitData>>> goalsHabit;
    private LiveData<Map<GoalData, List<TaskData>>> goalsTasks;

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

    LiveData<Map<GoalData, List<HabitData>>> getHabitsFromGoal(Long goalId) {
        if (goalsHabit == null)
            goalsHabit = goalsDao.getHabitsFromGoal(goalId);
        return goalsHabit;
    }

    LiveData<Map<GoalData, List<TaskData>>> getTasksFromGoal(Long goalId) {
        if (goalsTasks == null)
            goalsTasks = goalsDao.getTasksFromGoal(goalId);
        return goalsTasks;
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
