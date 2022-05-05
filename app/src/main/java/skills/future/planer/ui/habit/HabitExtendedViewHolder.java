package skills.future.planer.ui.habit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.day.views.habits.TextAdapter;
import skills.future.planer.ui.goals.ICustomViewHolder;
import skills.future.planer.ui.month.MonthFragment;

@Getter
public class HabitExtendedViewHolder extends ICustomViewHolder {
    private final TextView title;
    private final CircularProgressIndicator circularProgressIndicatorHabit;
    private final CircularProgressIndicator circularProgressIndicatorHabitDay;
    private final Context context;
    private final ChipGroup chipGroup;
    private final HabitViewModel habitViewModel;
    private final LifecycleOwner viewLifecycleOwner;

    public HabitExtendedViewHolder(View itemView, Context context, HabitViewModel habitViewModel,
                                   LifecycleOwner viewLifecycleOwner) {
        super(itemView);
        title = itemView.findViewById(R.id.habitTitleTextViewExtended);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorHabit);
        circularProgressIndicatorHabitDay = itemView.findViewById(R.id.circularProgressIndicatorHabitDay);
        chipGroup = itemView.findViewById(R.id.chipGroupWeek);
        this.context = context;
        this.habitViewModel = habitViewModel;
        this.viewLifecycleOwner = viewLifecycleOwner;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void setEveryThing(HabitData habitData) {
        this.title.setText(habitData.getTitle());

        setUpChipGroup(habitData);
        setUpCircularProgressIndicatorHabit(habitData);
        setUpCircularProgressIndicatorOfDays(habitData);
    }

    private void setUpChipGroup(HabitData habitData) {
        var days = habitData.getDaysOfWeek();
        for (int i = 0; i < days.length(); i++) {
            if (days.charAt(i) == '1') {
                var chip = new Chip(context);
                chip.setText(habitData.getDayWeekName(i));
                chip.setCheckable(false);
                chipGroup.addView(chip);
            }//todo rysuje zle chipy
        }
    }

    private void setUpCircularProgressIndicatorHabit(HabitData habitData) {
        circularProgressIndicatorHabit.setMaxProgress(100);
        circularProgressIndicatorHabit.setCurrentProgress(((double) habitData.getNumberOfDaysWhereHabitsWasDone()
                / (habitData.getNumberOfDaysWhereHabitsWasDone()
                + habitData.getNumberOfDaysWhereHabitsWasFailure())) * 100);
        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());
        if (circularProgressIndicatorHabit.getProgress() <= 40)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabit.getProgress() <= 75)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }

    private void setUpCircularProgressIndicatorOfDays(HabitData habitData) {
        circularProgressIndicatorHabitDay.setMaxProgress(habitData.getHabitDuration().getDaysNumber());
        circularProgressIndicatorHabitDay
                .setCurrentProgress(DatesParser.countDifferenceBetweenDays(habitData.getBeginCalendarDay(),
                        MonthFragment.getGlobalSelectedDate()));
                       /* ((double) habitData.getNumberOfDaysWhereHabitsWasDone()
                        / (habitData.getNumberOfDaysWhereHabitsWasDone()
                        + habitData.getNumberOfDaysWhereHabitsWasFailure())));*/

        HabitTextAdapter habitTextAdapter = new HabitTextAdapter();
        habitTextAdapter.setMaxProgress(circularProgressIndicatorHabitDay.getMaxProgress());
        circularProgressIndicatorHabitDay.setProgressTextAdapter(habitTextAdapter);

        if (circularProgressIndicatorHabitDay.getProgress() / circularProgressIndicatorHabitDay.getMaxProgress() <= 0.4)
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabitDay.getProgress() / circularProgressIndicatorHabitDay.getMaxProgress() <= 75)
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }
}
