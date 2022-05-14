package skills.future.planer.ui.habit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.day.views.habits.TextAdapter;
import skills.future.planer.ui.goals.pager.recycler.ICustomViewHolder;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;

@Getter
public class HabitExtendedViewHolder extends ICustomViewHolder {
    private final TextView title;
    private final CircularProgressIndicator circularProgressIndicatorHabit;
    private final CircularProgressIndicator circularProgressIndicatorHabitDay;
    private final Context context;
    private final ChipGroup chipGroup;
    private final Chip goalChip;
    private final Fragment fragment;
    private final TextView goalLabel;
    private final View constraintLayout;


    public HabitExtendedViewHolder(View itemView, Context context, Fragment fragment) {
        super(itemView);
        title = itemView.findViewById(R.id.habitTitleTextViewExtended);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorHabit);
        circularProgressIndicatorHabitDay = itemView.findViewById(R.id.circularProgressIndicatorHabitDay);
        chipGroup = itemView.findViewById(R.id.chipGroupWeek);
        this.context = context;
        this.fragment=fragment;
        goalChip = itemView.findViewById(R.id.goalChip);
        goalLabel = itemView.findViewById(R.id.goalText);
        constraintLayout = itemView.findViewById(R.id.constraintLayout);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        if (element instanceof HabitData habitData) {
            this.title.setText(habitData.getTitle());

            setUpChipGroup(habitData);
            setUpCircularProgressIndicatorHabit(habitData);
            setUpCircularProgressIndicatorOfDays(habitData);
            setUpGoalChip(habitData);
        }
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
        Date date = new Date(habitData.getBeginDay());

        Calendar today = Calendar.getInstance();
        today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);

        Date todayDate = today.getTime();

        long numberOfDays = TimeUnit.DAYS.convert(todayDate.getTime() - date.getTime(), TimeUnit.MILLISECONDS)+1;

        double currentProgress = ((double) habitData.getNumberOfDaysWhereHabitsWasDone() / numberOfDays) * 100;
        if (currentProgress > 100f) currentProgress = 100f;
        circularProgressIndicatorHabit.setCurrentProgress(currentProgress);

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
        if (CalendarDay.today().isAfter(habitData.getBeginCalendarDay()))
            circularProgressIndicatorHabitDay
                    .setCurrentProgress(DatesParser.countDifferenceBetweenDays(
                            habitData.getBeginCalendarDay(), CalendarDay.today())+1);
        else if(CalendarDay.today().isBefore(habitData.getBeginCalendarDay()))
            circularProgressIndicatorHabitDay.setCurrentProgress(0);
        else
            circularProgressIndicatorHabitDay.setCurrentProgress(1);
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

    private void setUpGoalChip(HabitData habitData){
        GoalsViewModel goalsViewModel = new ViewModelProvider(fragment).get(GoalsViewModel.class);
        GoalData goal = goalsViewModel.findById(habitData.getForeignKeyToGoal());
        if (goal != null){
            goalChip.setVisibility(View.VISIBLE);
            goalLabel.setVisibility(View.VISIBLE);
            String goalText = goal.getTitle();
            if(goalText.length()>30){
                goalText = goalText.substring(0,27)+"...";
            }
            goalChip.setText(goalText);
        }else {
            goalChip.setVisibility(View.INVISIBLE);
            goalLabel.setVisibility(View.INVISIBLE);
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 220, fragment.getResources().getDisplayMetrics());
            constraintLayout.getLayoutParams().height=px;
        }
    }
}
