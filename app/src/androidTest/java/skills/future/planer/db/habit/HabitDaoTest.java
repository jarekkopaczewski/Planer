package skills.future.planer.db.habit;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

import skills.future.planer.db.AppDatabase;

public class HabitDaoTest {
    private HabitDao habitDao;
    private AppDatabase db;
    private HabitData habit;

    @Test
    public void getNextNotification() throws Exception {
        var cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 0);
        assertEquals(habit, habitDao.getNextNotification(cal.getTimeInMillis()));
    }

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        habitDao = db.habitDao();
        var cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 22);
        cal.set(Calendar.MINUTE, 0);
        habit = new HabitData("test", "1111111", HabitDuration.Short, LocalDate.of(2022, 12, 1), cal);
        habitDao.insert(habit);
    }
}