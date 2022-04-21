package skills.future.planer.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import skills.future.planer.databinding.FragmentDayBinding;
import skills.future.planer.ui.day.views.daylist.DayTaskListFragment;

public class DayFragment extends Fragment {

    private DayViewModel dayViewModel;
    private FragmentDayBinding binding;
    private MyPagerAdapter myPagerAdapter;
    private MaterialCalendarView calendarView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);
        binding = FragmentDayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = binding.calendarView;

        ViewPager vpPager = binding.dayViewPager;
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), calendarView);
        myPagerAdapter.setPrimaryItem(container, 1, new DayTaskListFragment(calendarView));
        vpPager.setAdapter(myPagerAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}