package skills.future.planer.ui.goals;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lombok.Getter;
import skills.future.planer.ui.day.views.daylist.DayTaskListFragment;
import skills.future.planer.ui.habit.HabitBrowserFragment;

@Getter
public class GoalPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 2;
    private final HabitBrowserFragment habitBrowserFragment = new HabitBrowserFragment();
    private final DayTaskListFragment dayTaskListFragment = new DayTaskListFragment();

    public GoalPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return switch (position) {
            case 1 -> habitBrowserFragment;
            default -> dayTaskListFragment;
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case 0 -> "Zadania";
            case 1 -> "Nawyki";
            default -> null;
        };
    }
}
