package skills.future.planer.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import skills.future.planer.databinding.FragmentDayBinding;

public class DayFragment extends Fragment {

    private DayViewModel dayViewModel;
    private FragmentDayBinding binding;
    private MyPagerAdapter myPagerAdapter;
    private MaterialCalendarView calendarView;
    private FloatingActionButton fabDay;
    private TextView dayNumberView;
    private ViewPager vpPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);
        binding = FragmentDayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = binding.calendarView;
        fabDay = binding.dayFab;
        dayNumberView = binding.dayNumber;

        vpPager = binding.dayViewPager;
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(),calendarView);
        myPagerAdapter.setPrimaryItem(container, 1, myPagerAdapter.getTaskListFragment());
        vpPager.setAdapter(myPagerAdapter);
        vpPager.setCurrentItem(2);

        dateJumper();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        var selectedDay = calendarView.getSelectedDate();
        if (dayViewModel.checkIsTaskListView(vpPager) && selectedDay != null)
            dayViewModel.checkDateIsToday(selectedDay, fabDay, dayNumberView);
    }

    /**
     * Responsible for jump to today
     */
    private void dateJumper() {
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dayViewModel.checkPagerChange(position, vpPager, calendarView.getSelectedDate(), fabDay, dayNumberView);
            }

            @Override
            public void onPageSelected(int position) {
                dayViewModel.checkPagerChange(position, vpPager, calendarView.getSelectedDate(), fabDay, dayNumberView);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        dayNumberView.setText(String.valueOf(dayViewModel.getToday().getValue().getDay()));
        fabDay.setOnClickListener(v -> dayViewModel.returnToToday(calendarView, fabDay, dayNumberView));
        calendarView.setOnDateChangedListener(
                (widget, date, selected) -> {
                    if (dayViewModel.checkIsTaskListView(vpPager))
                        dayViewModel.checkDateIsToday(date, fabDay, dayNumberView);
                });

        dayViewModel.returnToToday(calendarView, fabDay, dayNumberView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}