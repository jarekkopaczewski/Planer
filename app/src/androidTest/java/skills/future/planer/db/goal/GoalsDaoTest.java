package skills.future.planer.db.goal;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitDuration;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

public class GoalsDaoTest {
    private GoalsDao goalsDao;
    private AppDatabase db;
    private HabitData habit;
    GoalData goal;
    private TaskData taskData;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        goalsDao = db.goalsDao();
        goal = new GoalData("tsego", "asfasf", LocalDate.of(2022, 1, 1));
        goal.setGoalId(goalsDao.insert(goal));
        habit = new HabitData("test", "1111111", HabitDuration.Short,
                LocalDate.of(2022, 1, 1), 12, 0, goal.getGoalId());
        habit.setHabitId(db.habitDao().insert(habit));
        taskData = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent,
                "test", "", CalendarDay.from(2022, 1, 1), CalendarDay.from(2022, 1, 3), goal.getGoalId());
        taskData.setTaskDataId(db.taskDataTabDao().insert(taskData));
    }

    @Test
    public void getGoalsWithHabits() {
//        var test = goalsDao.getHabitsFromGoal();
//        assertEquals(habit,goalsDao.getHabitsFromGoal().get(goal));
    }

    @Test
    public void testGetGoalsWithHabits() {
    }

    @Test
    public void getGoalsWithTasks() {
    }
}