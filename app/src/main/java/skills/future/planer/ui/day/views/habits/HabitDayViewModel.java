package skills.future.planer.ui.day.views.habits;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.stream.Collectors;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;

public class HabitDayViewModel extends ViewModel {
    private static HabitViewModel habitViewModel;
    private static HabitTotalAdapter habitTotalAdapter;
    private static LifecycleOwner viewLifecycleOwner;
    private static CircularProgressIndicator circularProgressIndicator;


    /**
     * Sets observer for list for concrete day
     */
    public void updateDate(CalendarDay date) {
        habitViewModel.getAllHabitDataFromDay(date)
                .observe(viewLifecycleOwner, habits -> habitTotalAdapter.setHabitsList(
                        habits.stream().filter(habitData -> habitData
                                .isDayOfWeekChecked(DatesParser.toLocalDate(date)))
                                .collect(Collectors.toList())));
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
                        circularProgressIndicator.setCurrentProgress(((double) progressDone / progressAll) * 100.0f);
                    else
                        circularProgressIndicator.setCurrentProgress(100.0f);
                    circularProgressIndicator.animate();
                    circularProgressIndicator.setProgressTextAdapter(new TextAdapter());

//                    if (circularProgressIndicator.getProgress() <= 40)
//                        circularProgressIndicator.setProgressColor(ContextCompat.getColor(requireContext(), R.color.bad));
//                    else if (circularProgressIndicator.getProgress() <= 75)
//                        circularProgressIndicator.setProgressColor(ContextCompat.getColor(requireContext(), R.color.mid));
//                    else
//                        circularProgressIndicator.setProgressColor(ContextCompat.getColor(requireContext(), R.color.good));
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

    public void setCircularProgress(CircularProgressIndicator circularProgressIndicator) {
        HabitDayViewModel.circularProgressIndicator = circularProgressIndicator;
    }
}