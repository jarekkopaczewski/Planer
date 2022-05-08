package skills.future.planer.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import lombok.Getter;
import skills.future.planer.MainActivity;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentDayBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.day.views.daylist.DayTaskListViewModel;
import skills.future.planer.ui.day.views.habits.HabitDayViewModel;
import skills.future.planer.ui.day.views.matrix.MatrixModelView;
import skills.future.planer.ui.month.MonthFragment;


@Getter
public class DayFragment extends Fragment {

    private DayViewModel dayViewModel;
    private DayTaskListViewModel dayTaskListViewModel;
    private HabitDayViewModel habitDayViewModel;
    private MatrixModelView matrixModelView;
    private FragmentDayBinding binding;
    private MyPagerAdapter myPagerAdapter;
    private MaterialCalendarView calendarView;
    private ViewPager vpPager;
    private ImageView todayIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);
        dayTaskListViewModel = new ViewModelProvider(this).get(DayTaskListViewModel.class);
        matrixModelView = new ViewModelProvider(this).get(MatrixModelView.class);
        habitDayViewModel = new ViewModelProvider(this).get(HabitDayViewModel.class);

        binding = FragmentDayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindingElements();

        calendarView.setSelectedDate(MonthFragment.getGlobalSelectedDate());

        createViewPager(container);

        setListenerForCreateTask();
        updateDayViewComponents();
        dateJumperToday();

        createPagerListener();

        setLongListenerToMonthFragment();
        return root;
    }

    /**
     * Binds components of view
     */
    private void bindingElements() {
        calendarView = binding.calendarPagerDay;
        vpPager = binding.dayViewPager;
        todayIcon = binding.todayIconDay;
    }

    /**
     * Creates view pager
     */
    private void createViewPager(ViewGroup container) {
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.setPrimaryItem(container, 1, myPagerAdapter.getTaskListFragment());
        vpPager.setAdapter(myPagerAdapter);
        vpPager.setCurrentItem(2);
    }

    /**
     * Sets a listener that receives data from a TaskListCreatorFragment
     */
    private void setListenerForCreateTask() {
        getParentFragmentManager().setFragmentResultListener("requestKey", this.getViewLifecycleOwner(), (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            DayTaskListViewModel.getMWordViewModel().insert(result);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        CalendarDay selectedDay = MonthFragment.getGlobalSelectedDate();
        checkToUpdateComponents(selectedDay);
        dayViewModel.returnToDate(calendarView, selectedDay);
    }

    /**
     * Creates listener for ViewPager
     */
    private void createPagerListener() {
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                checkToUpdateComponents(MonthFragment.getGlobalSelectedDate());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * Checks which page is needed to refresh
     */
    private void checkToUpdateComponents(CalendarDay selectedDay) {
        if (dayViewModel.checkIsTaskListView(vpPager)) {
            if (DayTaskListViewModel.getViewLifecycleOwner() != null)
                dayTaskListViewModel.updateDate(selectedDay);
        } else if (dayViewModel.checkIsMatrixView(vpPager))
            matrixModelView.setUpModels(selectedDay);
        else if (dayViewModel.checkIsHabitsView(vpPager))
            if (HabitDayViewModel.getViewLifecycleOwner() != null)
                habitDayViewModel.updateDate(selectedDay);
    }

    /**
     * Responsible for jump to today
     */
    private void dateJumperToday() {
        todayIcon.setOnClickListener(v -> {
            CalendarDay today = CalendarDay.today();
            MonthFragment.setGlobalSelectedDate(today);
            dayViewModel.returnToDate(calendarView, today);
            checkToUpdateComponents(today);
        });
    }

    /**
     * Checks to update page in view pager when date is changed
     */
    private void updateDayViewComponents() {
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            MonthFragment.setGlobalSelectedDate(date);
            checkToUpdateComponents(date);
        });
    }

    /**
     * Sets listener which navigate to month fragment
     */
    private void setLongListenerToMonthFragment() {
        calendarView.setOnDateLongClickListener((widget, date) -> {
            MonthFragment.setGlobalSelectedDate(date);
            MainActivity.getBottomView().setSelectedItemId(R.id.nav_month);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}