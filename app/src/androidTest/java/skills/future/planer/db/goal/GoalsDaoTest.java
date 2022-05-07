package skills.future.planer.db.goal;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.habit.HabitData;

public class GoalsDaoTest {
    private GoalsDao goalsDao;
    private AppDatabase db;
    private HabitData habit;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        goalsDao = db.goalsDao();
    }

    @Test
    public void getGoalsWithHabits() {
    }
}