package skills.future.planer.ui.day;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lombok.Getter;
import skills.future.planer.ui.day.views.daylist.DayTaskListFragment;
import skills.future.planer.ui.day.views.habits.HabitFragment;
import skills.future.planer.ui.day.views.matrix.MatrixFragment;
import skills.future.planer.ui.day.views.summary.SummaryFragment;

@Getter
public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 3;
    private final DayTaskListFragment taskListFragment = new DayTaskListFragment();
    private final MatrixFragment matrixFragment = new MatrixFragment();
    private final SummaryFragment summaryFragment = new SummaryFragment();
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
            case 1 -> taskListFragment;
            case 2 -> habitFragment;
            default -> matrixFragment;
            //default -> summaryFragment;
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            //case 0 -> "Podsumowanie tygodnia";
            case 0 -> "Macierz";
            case 1 -> "Lista zadań";
            case 2 -> "Nawyki";
            default -> null;
        };
    }
}
