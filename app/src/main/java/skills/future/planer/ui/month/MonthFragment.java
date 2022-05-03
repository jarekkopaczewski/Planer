package skills.future.planer.ui.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import skills.future.planer.MainActivity;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentMonthBinding;
import skills.future.planer.db.task.TaskDataViewModel;

/**
 * MonthFragment represents a Month fragment.
 */
public class MonthFragment extends Fragment {

    private static CalendarDay globalSelectedDate = null;
    /**
     * Calendar widget
     */
    private MaterialCalendarView materialCalendarView;

    /**
     * Databinding object
     */
    private FragmentMonthBinding binding;

    /**
     * TaskData view model
     */
    private TaskDataViewModel mWordViewModel;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * HashSets with days with a specific number of dots
     */
    private final HashSet<CalendarDay> oneDot = new HashSet<>();
    private final HashSet<CalendarDay> twoDot = new HashSet<>();
    private final HashSet<CalendarDay> threeDot = new HashSet<>();
    private final HashSet<CalendarDay> fourDot = new HashSet<>();

    /**
     * EventDecorators for specific number of dots
     */
    private final EventDecorator eventDecoratorOne = new EventDecorator(oneDot, 1);
    private final EventDecorator eventDecoratorTwo = new EventDecorator(twoDot, 2);
    private final EventDecorator eventDecoratorThree = new EventDecorator(threeDot, 3);
    private final EventDecorator eventDecoratorFour = new EventDecorator(fourDot, 4);
    private ImageView todayIcon;

    /**
     * View creation method.
     * Initialization of objects. Takes tasks from TaskDataViewModel.
     * Creates day decorators (today is marked, dots under date).
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMonthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        materialCalendarView = binding.calendar;
        todayIcon = binding.todayIconMonth;


        //setting current day as selected
        materialCalendarView.setDateSelected(CalendarDay.today(), true);
        mWordViewModel = ViewModelProviders.of(this).get(TaskDataViewModel.class);

        if (globalSelectedDate == null) {
            CalendarDay today = CalendarDay.today();
            globalSelectedDate = today;
            createListsWithDotsByTaskNumber(today);
        }
        createCalendarListeners();

        requireActivity().runOnUiThread(() -> {
            materialCalendarView.addDecorator(eventDecoratorOne);
            materialCalendarView.addDecorator(eventDecoratorTwo);
            materialCalendarView.addDecorator(eventDecoratorThree);
            materialCalendarView.addDecorator(eventDecoratorFour);
        });

        addDecoratorForDecoratingCurrentDate();
        dateJumperToday();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        setDateMarker(globalSelectedDate);
    }

    private void setDateMarker(CalendarDay dateMarker) {
        materialCalendarView.setCurrentDate(dateMarker, true);
        materialCalendarView.setDateSelected(materialCalendarView.getSelectedDate(), false);
        materialCalendarView.setDateSelected(dateMarker, true);
    }

    /**
     * Sets calendar listeners
     */
    private void createCalendarListeners() {
        materialCalendarView.setOnMonthChangedListener((widget, date) -> createListsWithDotsByTaskNumber(date));

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> globalSelectedDate = date);

        materialCalendarView.setOnDateLongClickListener((widget, date) -> {
            materialCalendarView.setSelectedDate(date);
            globalSelectedDate = date;
            MainActivity.getBottomView().setSelectedItemId(R.id.nav_day);
        });
    }

    /**
     * Adding decorator of today
     */
    private void addDecoratorForDecoratingCurrentDate() {

        materialCalendarView.addDecorator(new DayViewDecorator() {
            /**
             * Checks to decorate current date
             */
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.equals(CalendarDay.today());
            }

            /**
             * Decorates current day. Sets background using selector.xml
             */
            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(Objects.requireNonNull(
                        ContextCompat.getDrawable(materialCalendarView.getContext(), R.drawable.selector)));
            }
        });
    }


    /**
     * Creates lists for drawing dots
     */
    public void createListsWithDotsByTaskNumber(CalendarDay date) {
        executor.execute(() -> {

            oneDot.clear();
            twoDot.clear();
            threeDot.clear();
            fourDot.clear();

            LocalDate dateNextMonth = LocalDate.of(
                    date.getYear(),
                    date.getMonth(),
                    date.getDay()).plusMonths(1);

            LocalDate firstDay = LocalDate.of(
                    date.getYear(),
                    date.getMonth(),
                    1).minusMonths(1);

            LocalDate lastDay = LocalDate.of(
                    dateNextMonth.getYear(),
                    dateNextMonth.getMonth(),
                    dateNextMonth.lengthOfMonth());


            int value;

            for (LocalDate i = firstDay; i.isBefore(lastDay); i = i.plusDays(1)) {
                value = mWordViewModel.getNumberOfTaskByDate(CalendarDay.from(i));

                if (value > 0 && value < 5) {
                    oneDot.add(CalendarDay.from(i));
                } else if (value >= 5 && value < 10) {
                    twoDot.add(CalendarDay.from(i));
                } else if (value >= 10 && value < 15) {
                    threeDot.add(CalendarDay.from(i));
                } else if (value >= 15) {
                    fourDot.add(CalendarDay.from(i));
                }
            }

            eventDecoratorOne.setDates_tasks(oneDot);
            eventDecoratorTwo.setDates_tasks(twoDot);
            eventDecoratorThree.setDates_tasks(threeDot);
            eventDecoratorFour.setDates_tasks(fourDot);
            requireActivity().runOnUiThread(() -> materialCalendarView.invalidateDecorators());

        });
    }

    /**
     * Responsible for jump to today
     */
    private void dateJumperToday() {
        todayIcon.setOnClickListener(v -> {
            CalendarDay today = CalendarDay.today();
            MonthFragment.setGlobalSelectedDate(today);
            setDateMarker(globalSelectedDate);
        });
    }

    public static CalendarDay getGlobalSelectedDate() {
        return globalSelectedDate;
    }

    public static void setGlobalSelectedDate(CalendarDay globalSelectedDate) {
        MonthFragment.globalSelectedDate = globalSelectedDate;
    }

    /**
     * Destroying view
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
