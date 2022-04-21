package skills.future.planer.ui.day;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import skills.future.planer.ui.day.views.ScrollingFragment;
import skills.future.planer.ui.day.views.daylist.DayTaskListFragment;
import skills.future.planer.ui.day.views.matrix.MatrixFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 3;
    private final DayTaskListFragment taskListFragment;
    private final MatrixFragment matrixFragment = new MatrixFragment();
    private final ScrollingFragment scrollingFragment = new ScrollingFragment();

    public MyPagerAdapter(FragmentManager fragmentManager, MaterialCalendarView calendarView) {
        super(fragmentManager);
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
            case 2 -> scrollingFragment;
            default -> taskListFragment;
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return switch (position) {
            case 0 -> "Lista zadań";
            case 1 -> "Macierz";
            case 2 -> "Notatki";
            default -> null;
        };
    }
}
