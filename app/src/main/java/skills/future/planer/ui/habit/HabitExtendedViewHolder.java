package skills.future.planer.ui.habit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.day.views.habits.TextAdapter;
import skills.future.planer.ui.goals.ICustomViewHolder;

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
        chipGroup.removeAllViews();
        var days = habitData.getDaysOfWeek();
        for (int i = 0; i < days.length(); i++) {
            if (days.charAt(i) == '1') {
                var chip = new Chip(context);
                chip.setText(habitData.getDayWeekName(i));
                chip.setCheckable(false);
                chipGroup.addView(chip);
            }
        }
    }

    private void setUpCircularProgressIndicatorHabit(HabitData habitData) {
        circularProgressIndicatorHabit.setMaxProgress(100);

        double currentProgress = countCurrentProgress(habitData);

        circularProgressIndicatorHabit.setCurrentProgress(currentProgress);

        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());
        if (circularProgressIndicatorHabit.getProgress() <= 40)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabit.getProgress() <= 75)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }

    /**
     * Counts the progression for the habit from the start date to today
     */
    private double countCurrentProgress(HabitData habitData) {
        LocalDate today = LocalDate.now();

        LocalDate beginDate = DatesParser.toLocalDate(habitData.getBeginCalendarDay());

        double days = 0;
        double doneHabitDays = 0;

        for (LocalDate i = beginDate; i.isBefore(today) || i.isEqual(today); i = i.plusDays(1)) {
            if (habitData.isDayOfWeekChecked(i)) {
                days++;
                if (habitData.isHabitDone(DatesParser.toCalendarDay(i)))
                    doneHabitDays++;
            }
        }
        return (doneHabitDays / days) * 100;
    }

    private void setUpCircularProgressIndicatorOfDays(HabitData habitData) {
        circularProgressIndicatorHabitDay.setMaxProgress(habitData.getHabitDuration().getDaysNumber());
        if (CalendarDay.today().isAfter(habitData.getBeginCalendarDay()))
            circularProgressIndicatorHabitDay
                    .setCurrentProgress(DatesParser.countDifferenceBetweenDays(
                            habitData.getBeginCalendarDay(), CalendarDay.today()) + 1);
        else if (CalendarDay.today().isBefore(habitData.getBeginCalendarDay()))
            circularProgressIndicatorHabitDay.setCurrentProgress(0);
        else
            circularProgressIndicatorHabitDay.setCurrentProgress(1);

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
