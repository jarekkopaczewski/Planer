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

import org.threeten.bp.LocalDate;

import lombok.Getter;
import skills.future.planer.databinding.FragmentDayBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.day.views.daylist.DayTaskListViewModel;
import skills.future.planer.ui.day.views.matrix.MatrixModelView;
import skills.future.planer.ui.slideshow.SlideshowViewModel;

@Getter
public class DayFragment extends Fragment {

    private DayViewModel dayViewModel;
    private DayTaskListViewModel dayTaskListViewModel;
    private MatrixModelView matrixModelView;
    private FragmentDayBinding binding;
    private MyPagerAdapter myPagerAdapter;
    private MaterialCalendarView calendarView;
    private FloatingActionButton fabDay;
    private TextView dayNumberView;
    private ViewPager vpPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);
        dayTaskListViewModel = new ViewModelProvider(this).get(DayTaskListViewModel.class);
        matrixModelView = new ViewModelProvider(this).get(MatrixModelView.class);

        binding = FragmentDayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindingElements();

        calendarView.setSelectedDate(LocalDate.now());

        createViewPager(container);

        setListenerForCreateTask();
        createPagerListener();
        dateJumper();
        updateDayViewComponents();

        return root;
    }

    /**
     * Binds components of view
     */
    private void bindingElements() {
        calendarView = binding.calendarView;
        fabDay = binding.dayFab;
        dayNumberView = binding.dayNumber;
        vpPager = binding.dayViewPager;
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
        var selectedDay = calendarView.getSelectedDate();
        if (dayViewModel.checkIsTaskListView(vpPager) && selectedDay != null) {
            dayViewModel.checkDateIsToday(selectedDay, fabDay, dayNumberView);
            dayTaskListViewModel.updateDate(selectedDay);
        }
        if (dayViewModel.checkIsMatrixView(vpPager) && selectedDay != null)
            matrixModelView.setUpModels(selectedDay);
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
                dayViewModel.checkPagerChange(position,
                        vpPager,
                        calendarView.getSelectedDate(),
                        fabDay,
                        dayNumberView,
                        matrixModelView,
                        dayTaskListViewModel);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * Responsible for jump to today
     */
    private void dateJumper() {
        dayNumberView.setText(String.valueOf(dayViewModel.getToday().getValue().getDay()));
        fabDay.setOnClickListener(v -> {
            dayViewModel.returnToToday(calendarView, fabDay, dayNumberView);
            dayTaskListViewModel.updateDate(dayViewModel.getToday().getValue());
        });
        dayViewModel.returnToToday(calendarView, fabDay, dayNumberView);
    }

    /**
     * Checks to change visibility of the fab button
     * Checks to update Matrix view
     * Checks to update TaskList view
     */
    private void updateDayViewComponents() {
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (dayViewModel.checkIsTaskListView(vpPager)) {
                dayViewModel.checkDateIsToday(date,
                        fabDay,
                        dayNumberView);
                dayTaskListViewModel.updateDate(date);
            }
            if (dayViewModel.checkIsMatrixView(vpPager))
                matrixModelView.setUpModels(date);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}