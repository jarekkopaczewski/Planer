package skills.future.planer.ui.day.views.habits;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.stream.Collectors;

import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;

public class HabitDayViewModel extends ViewModel {
    private static HabitViewModel habitViewModel;
    private static HabitTotalAdapter habitTotalAdapter;
    private static LifecycleOwner viewLifecycleOwner;


    /**
     * Sets observer for list for concrete day
     */
    public void updateDate(CalendarDay date) {
        habitViewModel.getAllHabitDataFromDay(date)
                .observe(viewLifecycleOwner, habits -> habitTotalAdapter.setHabitsList
                        (habits.stream().filter(habitData -> habitData
                                .isDayOfWeekChecked(DatesParser.toLocalDate(date)))
                                .collect(Collectors.toList())));
    }

    public HabitViewModel getHabitViewModel() {
        return habitViewModel;
    }

    public void setHabitViewModel(HabitViewModel habitViewModel) {
        HabitDayViewModel.habitViewModel = habitViewModel;
    }

    public static LifecycleOwner getViewLifecycleOwner() {
        return viewLifecycleOwner;
    }

    public void setViewLifecycleOwner(LifecycleOwner viewLifecycleOwner) {
        HabitDayViewModel.viewLifecycleOwner = viewLifecycleOwner;
    }

    public HabitTotalAdapter getHabitTotalAdapter() {
        return habitTotalAdapter;
    }

    public void setHabitTotalAdapter(HabitTotalAdapter habitTotalAdapter) {
        HabitDayViewModel.habitTotalAdapter = habitTotalAdapter;
    }
}