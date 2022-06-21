package skills.future.planer.ui.day;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lombok.Getter;
import skills.future.planer.ui.day.views.daylist.DayTaskListFragment;
import skills.future.planer.ui.day.views.habits.HabitFragment;
import skills.future.planer.ui.day.views.matrix.MatrixFragment;
import skills.future.planer.ui.day.views.summary.DaySummaryFragment;

@Getter
public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 4;
    private final DayTaskListFragment taskListFragment = new DayTaskListFragment();
    private final MatrixFragment matrixFragment = new MatrixFragment();
    private final DaySummaryFragment summaryFragment = new DaySummaryFragment();
    private final HabitFragment habitFragment = new HabitFragment();

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
            default -> summaryFragment;
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case 0 -> "Podsumowanie tygodnia";
            case 1 -> "Macierz";
            case 2 -> "Lista zadań";
            case 3 -> "Nawyki";
            default -> null;
        };
    }
}
