package skills.future.planer.ui.habit;

import java.util.Comparator;

import skills.future.planer.db.habit.HabitData;

public class HabitComparator implements Comparator<HabitData> {
    @Override
    public int compare(HabitData x, HabitData y) {
        long result = x.getBeginDay() - y.getBeginDay();
        if( result < 0 )
            return 1;
        else if( result > 0 )
            return -1;
        else
            return 0;
    }
}
