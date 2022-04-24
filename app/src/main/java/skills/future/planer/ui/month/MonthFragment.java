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
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private List<CalendarDay> days;
    private HashMap<CalendarDay,Integer> dayTasks2;

    /**
     * Initialization of HashMap and HashSets
     */
    public MonthFragment() {
        dayTasks = new HashMap<>();
        dayTasks2 = new HashMap<>();
        category1 = new HashSet<>();
        category2 = new HashSet<>();
        category3 = new HashSet<>();
        category4 = new HashSet<>();
        days = new ArrayList<>();
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
        CalendarDay today = CalendarDay.today();
        test_dots(today);

        //observes TaskDataViewModel, runs setDots method
       // mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> this.setDotsTaskNumber(taskData));

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
//                System.out.println();
//                int day = date.getDay();
//                int month = date.getMonth();
//                int year = date.getYear();
//
//
//                CalendarDay next = CalendarDay.from(year,month+1,day);
//                CalendarDay previous = CalendarDay.from(year,month-1,day);
//                CalendarDay selected =  materialCalendarView.getSelectedDate();
//                materialCalendarView.setSelectionColor(255);
//                materialCalendarView.selectRange(previous,next);
//                days = materialCalendarView.getSelectedDates();
//                System.out.println(days);
//                materialCalendarView.clearSelection();
//                materialCalendarView.setSelectionColor(-16744817);
//                materialCalendarView.setDateSelected(selected,true);
                 test_dots(date);





            }
        });




        //TODO metoda do pobierania liczby zadań dla @Paweł Helisz
        // System.out.println(mWordViewModel.getNumberOfTaskByDate(Calendar.today()));

        //todo - add choice for dots view - number of task/only Urgent/Important task
        // mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> this.setDotsTaskCategory(taskData));

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
    public void setDotsTaskNumber(List<TaskData> words) {

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

    /**
     * Method sets dots under dates based on category: Important/Urgent
     * @param words tasks list
     */
    public void setDotsTaskCategory(List<TaskData> words){

        dayTasks.clear();
        category1.clear();
        category2.clear();
        category3.clear();
        category4.clear();

        for (TaskData taskData_instance : words) {
            CalendarDay key = taskData_instance.getStartingCalendarDate();
            if(taskData_instance.getPriorities().getPriorityLvl()==0 &&
               taskData_instance.getTimePriority().getTimePriorityLvl()==0){
            if(!category1.contains(key))
            category1.add(key);
        }}
        materialCalendarView.addDecorator(new EventDecorator(category1, 1));
    }

    public void test_dots(CalendarDay date){

        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();

        CalendarDay next = CalendarDay.from(year,month+1,day);
        CalendarDay previous = CalendarDay.from(year,month-1,day);
        CalendarDay selected =  materialCalendarView.getSelectedDate();
        materialCalendarView.setSelectionColor(255);
        materialCalendarView.selectRange(date,next);
        days = materialCalendarView.getSelectedDates();
        ArrayList days2 = new ArrayList();
        for (CalendarDay calendarDay : days) {
            days2.add(CalendarDay.from(calendarDay.getYear(),calendarDay.getMonth(),calendarDay.getDay()));
        }
        //System.out.println(days);
        materialCalendarView.clearSelection();
        materialCalendarView.setSelectionColor(-16744817);
        materialCalendarView.setDateSelected(selected,true);
        addDots(days2);

    }

    public void addDots(List<CalendarDay> days){
        int numberOfTasks = 0;

        dayTasks2.clear();
        category1.clear();
        category2.clear();
        category3.clear();
        category4.clear();

        for(CalendarDay day : days){
            numberOfTasks = mWordViewModel.getNumberOfTaskByDate(day);
            if(numberOfTasks>0){
                dayTasks2.put(day,numberOfTasks);
            }
            //numberOfTasks=0;
        }

        for (Map.Entry<CalendarDay, Integer> entry : dayTasks2.entrySet()) {
            CalendarDay key = entry.getKey();
            Integer value = entry.getValue();
            //System.out.println(key+" "+value);

            if (value > 0 && value < 4) {
                category1.add(key);
            } else if (value > 4 && value < 10) {
                category2.add(key);
            } else if (value > 10 && value < 15) {
                category3.add(key);
            } else if (value > 15) {
                category4.add(key);
            }

            materialCalendarView.addDecorator(new EventDecorator(category1, 1));
            materialCalendarView.addDecorator(new EventDecorator(category2, 2));
            materialCalendarView.addDecorator(new EventDecorator(category3, 3));
            materialCalendarView.addDecorator(new EventDecorator(category4, 4));

    }}


}
