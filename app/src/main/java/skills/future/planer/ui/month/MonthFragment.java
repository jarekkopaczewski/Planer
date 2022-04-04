package skills.future.planer.ui.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentMonthBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;

/**
 * MonthFragment represents a Month fragment.
 *
 * @author Paweł Helisz
 * @version 0.2.2
 * @since 0.2.1
 */
@Getter
@Setter
public class MonthFragment extends Fragment {

    /**
     * Calendar widget
     */
    private MaterialCalendarView materialCalendarView;

    /**
     * Month view model - extends ViewModel
     */
    private MonthViewModel monthViewModel;

    /**
     * Databinding object
     */
    private FragmentMonthBinding binding;

    /**
     * TaskData view model
     */
    private TaskDataViewModel mWordViewModel;

    /**
     * Hash map of tasks.
     * Key - CalendarDay - day of a task
     * Value - Integer - number of tasks in that day
     */
    private HashMap<CalendarDay, Integer> dayTasks;

    /**
     * Hash set of CalendarDay which fits into given category
     */
    private HashSet<CalendarDay> category1;
    private HashSet<CalendarDay> category2;
    private HashSet<CalendarDay> category3;
    private HashSet<CalendarDay> category4;

    /**
     * Initialization of HashMap and HashSets
     */
    public MonthFragment() {
        dayTasks = new HashMap<>();
        category1 = new HashSet<>();
        category2 = new HashSet<>();
        category3 = new HashSet<>();
        category4 = new HashSet<>();
    }

    /**
     * Initial creation of the fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * View creation method.
     * Initialization of objects. Takes tasks from TaskDataViewModel.
     * Creates day decorators (today is marked, dots under date).
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);
        binding = FragmentMonthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        materialCalendarView = binding.calendarView;

        //setting current day as selected
        materialCalendarView.setDateSelected(CalendarDay.today(), true);
        mWordViewModel = ViewModelProviders.of(this).get(TaskDataViewModel.class);

        //observes TaskDataViewModel, runs setDots method
        mWordViewModel.getAllWords().observe(this.getViewLifecycleOwner(), taskData -> this.setDots(taskData));

        //adding decorator
        materialCalendarView.addDecorator(new DayViewDecorator() {

            /**
             * Should decorate current date
             * @param day
             * @return current day
             */
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.toString().equals(CalendarDay.today().toString());
            }

            /**
             * Decorates current day. Sets background using selector.xml
             * @param view day view
             */
            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(materialCalendarView.getContext(), R.drawable.selector)));
            }
        });
        return root;
    }

    /**
     * Destroying view
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Method sets dots under dates which have tasks added to them
     *
     * @param words List of TaskData - contains starting and ending date
     */
    public void setDots(List<TaskData> words) {

        //clearing sets
        dayTasks.clear();
        category1.clear();
        category2.clear();
        category3.clear();
        category4.clear();

        //adding value to a key (day) if contains tasks
        for (TaskData taskData_instance : words) {
            CalendarDay key = taskData_instance.getStartingCalendarDate();
            Integer value = dayTasks.get(key);
            if (value == null) {
                value = 1;
            } else {
                value++;
            }
            dayTasks.put(key, value);
        }

        //putting days into category
        for (Map.Entry<CalendarDay, Integer> entry : dayTasks.entrySet()) {
            CalendarDay key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key);

            if (value > 0 && value < 4) {
                category1.add(key);
            } else if (value > 4 && value < 10) {
                category2.add(key);
            } else if (value > 10 && value < 15) {
                category3.add(key);
            } else if (value > 15) {
                category4.add(key);
            }
        }
        //adding decorators to decorate sets of categorized days
        materialCalendarView.addDecorator(new EventDecorator(category1, 1));
        materialCalendarView.addDecorator(new EventDecorator(category2, 2));
        materialCalendarView.addDecorator(new EventDecorator(category3, 3));
        materialCalendarView.addDecorator(new EventDecorator(category4, 4));
    }
}