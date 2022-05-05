package skills.future.planer.db.habit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Calendar;

import skills.future.planer.db.DataBaseException;

@RunWith(AndroidJUnit4.class)
public class HabitDataTest {
    HabitData habit;

    @Before
    public void setUp() throws DataBaseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MONTH, 0);
        habit = new HabitData("test", "1111111", HabitDuration.Short,
                LocalDate.of(2022, 1, 1), calendar);
    }

    @Test
    public void checkDaysOfWeekNoCheckedDays() throws DataBaseException {
        String days = "0000000";
        habit.setDaysOfWeek(days);
        assertEquals(habit.getDaysOfWeek(), days);
    }

    @Test(expected = DataBaseException.class)
    public void checkDaysOfWeekTooMuchCheckedDays() throws DataBaseException {
        String days = "00000001";
        habit.setDaysOfWeek(days);
        assertEquals(habit.getDaysOfWeek(), days);
    }

    @Test
    public void checkChangingHabitState() throws DataBaseException {
        habit.setHabitDoneIn(CalendarDay.from(2022, 1, 1));
        habit.setHabitDoneIn(CalendarDay.from(2022, 2, 1));
        habit.setHabitDoneIn(CalendarDay.from(2022, 3, 31));
        var expected = "1" +
                generate(() -> "0").limit(30).collect(joining()) +
                "1" +
                generate(() -> "0").limit(habit.getHabitDuration().getDaysNumber() - 33).collect(joining()) +
                "1";
        var actual = habit.getDayChecking();
        assertEquals(expected, actual);
    }

    @Test(expected = DataBaseException.class)
    public void checkChangingHabitStateNotSetDayChecking() throws DataBaseException {
        habit = new HabitData();
        habit.setHabitDoneIn(CalendarDay.from(2022, 1, 1));
    }

    @Test(expected = DataBaseException.class)
    public void checkChangingHabitStateWrongDate() throws DataBaseException {
        habit = new HabitData();
        habit.setHabitDoneIn(CalendarDay.from(2022, 5, 1));
    }

    @Test
    public void getAllDaysWhereHabitsWasDone() throws DataBaseException {
        assertTrue(habit.getAllDaysWhereHabitsWasDone().isEmpty());
        habit.setHabitDoneIn(CalendarDay.from(2022, 1, 1));
        habit.setHabitDoneIn(CalendarDay.from(2022, 2, 1));
        habit.setHabitDoneIn(CalendarDay.from(2022, 3, 31));
        assertEquals(3, habit.getAllDaysWhereHabitsWasDone().size());
    }
}