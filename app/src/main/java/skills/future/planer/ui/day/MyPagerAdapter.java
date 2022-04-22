package skills.future.planer.ui.day;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import lombok.Getter;
import skills.future.planer.ui.day.views.habits.HabitFragment;
import skills.future.planer.ui.day.views.notepad.ScrollingFragment;
import skills.future.planer.ui.day.views.daylist.DayTaskListFragment;
import skills.future.planer.ui.day.views.matrix.MatrixFragment;

@Getter
public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 5;
    private final DayTaskListFragment taskListFragment;
    private final MatrixFragment matrixFragment;
    private final ScrollingFragment scrollingFragment = new ScrollingFragment();
    private final ScrollingFragment scrollingFragment2 = new ScrollingFragment();
    private final HabitFragment habitFragment = new HabitFragment();
    private final MaterialCalendarView calendarView;

    public MyPagerAdapter(FragmentManager fragmentManager, MaterialCalendarView calendarView) {
        super(fragmentManager);
        this.calendarView = calendarView;
        matrixFragment = new MatrixFragment(calendarView);
        taskListFragment = new DayTaskListFragment(calendarView);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        return switch (position) {
            case 1 -> matrixFragment;
            case 2 -> taskListFragment;
            case 3 -> habitFragment;
            case 4 -> scrollingFragment2;
            default -> scrollingFragment;
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case 0 -> "Notatki";
            case 1 -> "Macierz";
            case 2 -> "Lista zadań";
            case 3 -> "Nawyki";
            case 4 -> "Notatki";
            default -> null;
        };
    }
}
