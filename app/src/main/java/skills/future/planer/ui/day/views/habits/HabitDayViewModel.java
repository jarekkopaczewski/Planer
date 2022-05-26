package skills.future.planer.ui.day.views.habits;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.stream.Collectors;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;

public class HabitDayViewModel extends ViewModel {
    private static HabitViewModel habitViewModel;
    private static HabitTotalAdapter habitTotalAdapter;
    private static LifecycleOwner viewLifecycleOwner;
    private static CircularProgressIndicator circularProgressIndicator;
    private static TextView status;
    private static Context context;

    public static void setStatus(TextView status) {
        HabitDayViewModel.status = status;
    }

    public static LifecycleOwner getViewLifecycleOwner() {
        return viewLifecycleOwner;
    }

    public void setViewLifecycleOwner(LifecycleOwner viewLifecycleOwner) {
        HabitDayViewModel.viewLifecycleOwner = viewLifecycleOwner;
    }

    public HabitViewModel getHabitViewModel() {
        return habitViewModel;
    }

    public void setHabitViewModel(HabitViewModel habitViewModel) {
        HabitDayViewModel.habitViewModel = habitViewModel;
    }

    public HabitTotalAdapter getHabitTotalAdapter() {
        return habitTotalAdapter;
    }

    public void setHabitTotalAdapter(HabitTotalAdapter habitTotalAdapter) {
        HabitDayViewModel.habitTotalAdapter = habitTotalAdapter;
    }

    public void setCircularProgress(CircularProgressIndicator circularProgressIndicator) {
        HabitDayViewModel.circularProgressIndicator = circularProgressIndicator;
        circularProgressIndicator.setProgressColor(ContextCompat.getColor(context, R.color.mid));
    }

    public void setContext(Context context) {
        HabitDayViewModel.context = context;
    }

    /**
     * Sets observer for list for concrete day
     */
    public void updateDate(CalendarDay date) {
        habitViewModel.getAllHabitDataFromDay(date)
                .observe(viewLifecycleOwner, habits -> {
                    habitTotalAdapter.setHabitsList(
                            habits.stream().filter(habitData -> habitData
                                    .isDayOfWeekChecked(DatesParser.toLocalDate(date)))
                                    .collect(Collectors.toList()));
                    if (habits.size() == 0) {
                        circularProgressIndicator.setVisibility(View.INVISIBLE);
                        status.setVisibility(View.VISIBLE);
                    } else {
                        circularProgressIndicator.setVisibility(View.VISIBLE);
                        status.setVisibility(View.INVISIBLE);
                    }
                });

        habitViewModel.getAllHabitDataFromDay(date).observe(
                viewLifecycleOwner, habitData -> {
                    Integer progressDone = 0;
                    Integer progressAll = 0;
                    for (HabitData habit : habitData.stream().filter(habits -> habits
                            .isDayOfWeekChecked(DatesParser.toLocalDate(date)))
                            .collect(Collectors.toList())) {
                        progressDone += (habit.isHabitDone(date) ? 1 : 0);
                        progressAll += 1;
                    }
                    if (progressAll > 0.5)
                        circularProgressIndicator.setProgress(((double) progressDone / progressAll) * 100.0f, 100f);
                    else
                        circularProgressIndicator.setProgress(100.0f, 100f);
                });
    }
}