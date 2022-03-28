package skills.future.planer.ui.month;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentMonthBinding;
import skills.future.planer.db.task.TaskData;

@Getter
@Setter
public class MonthFragment extends Fragment {

    private TextView textView;
    private MaterialCalendarView materialCalendarView;
    private String date = "0"; //wybrany dzień z kalendarza
    private MonthViewModel monthViewModel;
    private FragmentMonthBinding binding;
    HashSet<CalendarDay> dates = new HashSet<CalendarDay>();
    HashSet<TaskData> calendarTaskDates = new HashSet<>();
    HashMap<CalendarDay, Integer> dayTasks = new HashMap<>();

    public MonthFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);
        binding = FragmentMonthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        materialCalendarView = binding.calendarView;
        materialCalendarView.setDateSelected(CalendarDay.today(), true);

        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.toString().equals(CalendarDay.today().toString());
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(materialCalendarView.getContext(), R.drawable.selector)));
            }
        });

        int[] threeColors = {
                Color.rgb(0, 0, 255),
                Color.rgb(0, 255, 0),
                Color.rgb(255, 0, 0),

        };
        dates.add(CalendarDay.from(2022,3,28));
        dates.add(CalendarDay.from(2022,3,23));
        dates.add(CalendarDay.from(2022,3,22));
        dates.add(CalendarDay.from(2022,3,11));
        // createDayTasks(calendarTaskDates);
        materialCalendarView.addDecorator(new EventDecorator(dates, threeColors));
        dates.clear();
        threeColors = new int[]{
                Color.rgb(0, 0, 255)
        };
        dates.add(CalendarDay.from(2022,3,31));
        dates.add(CalendarDay.from(2022,3,1));
        dates.add(CalendarDay.from(2022,3,13));
        dates.add(CalendarDay.from(2022,3,17));
        materialCalendarView.addDecorator(new EventDecorator(dates, threeColors));
        dates.clear();
        threeColors = new int[]{
                Color.rgb(0, 0, 255),
                Color.rgb(0, 255, 0),
        };
        dates.add(CalendarDay.from(2022,3,15));
        dates.add(CalendarDay.from(2022,3,14));
        dates.add(CalendarDay.from(2022,3,6));
        dates.add(CalendarDay.from(2022,3,3));
        materialCalendarView.addDecorator(new EventDecorator(dates, threeColors));
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
           // dates.add(date);
            //materialCalendarView.addDecorator(new EventDecorator(dates, threeColors));

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void createDayTasks(Collection<TaskData> taskData) {
        for (TaskData taskData_instance : taskData) {
            CalendarDay key = taskData_instance.getStartingDate();

            Integer value = dayTasks.get(key);
            if (value == null) {
                value = 1;
            } else {
                value++;
            }
            dayTasks.put(key, value);
        }
    }
}