package skills.future.planer.db.goal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Map;

import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitRepository;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataRepository;

public class GoalsViewModel extends AndroidViewModel {
    private final GoalRepository goalRepository;
    private final HabitRepository habitRepository;
    private final TaskDataRepository taskDataRepository;

    public GoalsViewModel(@NonNull Application application) {
        super(application);
        goalRepository = new GoalRepository(application);
        this.habitRepository = new HabitRepository(application);
        this.taskDataRepository = new TaskDataRepository(application);
    }

    /**
     * @return reference to list of all habits
     */
    public LiveData<List<GoalData>> getAllGoals() {
        return goalRepository.getAllGoals();
    }

    public LiveData<Map<GoalData, HabitData>> getHabitsFromGoal(Long goalId) {
        return goalRepository.getHabitsFromGoal(goalId);
    }

    public LiveData<Map<GoalData, TaskData>> getTasksFromGoal(Long goalId) {
        return goalRepository.getTasksFromGoal(goalId);
    }

    public Map<GoalData, HabitData> getHabitsFromGoalWithoutLiveData(Long goalId) {
        return goalRepository.getHabitsFromGoalWithoutLiveData(goalId);
    }

    public Map<GoalData, TaskData> getTasksFromGoalWithoutLiveData(Long goalId) {
        return goalRepository.getTasksFromGoalWithoutLiveData(goalId);
    }

    /**
     * Method insert habit to database
     *
     * @param goalData which will be inserted
     */
    public void insert(GoalData goalData) {
        goalRepository.insert(goalData);
    }

    /**
     * Method edit habit in database
     *
     * @param goalData which will be updated
     */
    public void edit(GoalData goalData) {
        goalRepository.edit(goalData);
    }

    /**
     * Method delete habit from database
     *
     * @param goalData which will be deleted
     */
    public void delete(GoalData goalData) {
        var habitMap = getHabitsFromGoalWithoutLiveData(goalData.getGoalId());
        habitMap.values().stream()
                .filter(habitData -> habitData.getForeignKeyToGoal().equals(goalData.getGoalId()))
                .forEach(habitData -> {
                    habitData.setForeignKeyToGoal(null);
                    habitRepository.edit(habitData);
                });
        var taskMap = getTasksFromGoalWithoutLiveData(goalData.getGoalId());
        taskMap.values().stream()
                .filter(taskData -> taskData.getForeignKeyToGoal().equals(goalData.getGoalId()))
                .forEach(taskData -> {
                    taskData.setForeignKeyToGoal(null);
                    taskDataRepository.edit(taskData);
                });
        goalRepository.delete(goalData);
    }

    public GoalData findById(Long goalId) {
        return goalRepository.findById(goalId);
    }
}
