package skills.future.planer.ui.day.views.habits;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;
import java.util.stream.Collectors;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.month.MonthFragment;

public class HabitDayViewModel extends ViewModel {
    private static HabitViewModel habitViewModel;
    private static HabitTotalAdapter habitTotalAdapter;
    private static LifecycleOwner viewLifecycleOwner;
    private static CircularProgressIndicator progressBar;
    private static Context context;


    /**
     * Sets observer for list for concrete day
     */
    public void updateDate(CalendarDay date) {
        habitViewModel.getAllHabitDataFromDay(date)
                .observe(viewLifecycleOwner, habits -> {

                    List<HabitData> list = habits.stream().filter(habitData -> habitData
                                    .isDayOfWeekChecked(DatesParser.toLocalDate(date)))
                            .collect(Collectors.toList());

                    float value = (list.stream()
                            .filter(h -> h.isHabitDone(MonthFragment.getGlobalSelectedDate())).count()
                            / (float) list.size()) * 100f;

                    progressBar.setCurrentProgress(value);

                    if (value <= 40.0f) {
                        progressBar.setProgressColor(ContextCompat.getColor(context, R.color.bad));
                    } else if (value <= 75.0f) {
                        progressBar.setProgressColor(ContextCompat.getColor(context, R.color.mid));
                    } else {
                        progressBar.setProgressColor(ContextCompat.getColor(context, R.color.good));
                    }

                    habitTotalAdapter.setHabitsList(list);
                });
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

    public void setContext(Context context) {
        HabitDayViewModel.context = context;
    }

    public static void setProgressBar(CircularProgressIndicator progressBar) {
        HabitDayViewModel.progressBar = progressBar;
    }
}