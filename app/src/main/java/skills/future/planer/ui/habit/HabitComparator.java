package skills.future.planer.ui.habit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Comparator;

import skills.future.planer.db.habit.HabitData;

public class HabitComparator implements Comparator<HabitData> {

    private long millis;

    public HabitComparator(){
        Calendar calendar = Calendar.getInstance();
        millis = calendar.getTimeInMillis();
    }
    @Override
    public int compare(HabitData x, HabitData y) {
        if( x.getEndDay() < millis) return 1;
        long result = x.getEndDay() - y.getEndDay();
        return result < 0 ? 1 : result > 0 ? -1 : 0;
    }
}
