package skills.future.planer.ui.habit.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.day.views.habits.TextAdapter;
import skills.future.planer.ui.goals.pager.recycler.ICustomViewHolder;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;
import skills.future.planer.ui.habit.HabitTextAdapter;

public class HabitViewHolder extends ICustomViewHolder {
    protected final TextView title;
    private final CircularProgressIndicator circularProgressIndicatorHabit;
    protected CircularProgressIndicator circularProgressIndicatorHabitDay;
    private final Context context;
    private final HabitTextAdapter habitTextAdapter = new HabitTextAdapter();

    public HabitViewHolder(View itemView, Context context, ComponentActivity activity) {
        super(itemView);
        this.context = context;
        title = itemView.findViewById(R.id.habitTitleTextViewExtended);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorHabit);
        circularProgressIndicatorHabitDay = itemView.findViewById(R.id.circularProgressIndicatorHabitDay);
    }

    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        HabitData h = (HabitData) element;
        this.title.setText(h.getTitle());
        setUpCircularProgressIndicatorHabit(h);
        setUpCircularProgressIndicatorOfDays(h);
    }

    protected void setUpCircularProgressIndicatorHabit(HabitData habitData) {
        circularProgressIndicatorHabit.setMaxProgress(100);

        double currentProgress = countCurrentProgress(habitData);

        circularProgressIndicatorHabit.setCurrentProgress(currentProgress);

        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());
        if (circularProgressIndicatorHabit.getProgress() <= 40.0)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabit.getProgress() <= 75.0)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }

    protected void setUpCircularProgressIndicatorOfDays(HabitData habitData) {
        circularProgressIndicatorHabitDay.setMaxProgress(habitData.getHabitDuration().getDaysNumber());

        if (CalendarDay.today().isAfter(habitData.getEndCalendarDay()))
            circularProgressIndicatorHabitDay.setCurrentProgress(habitData.getHabitDuration().getDaysNumber());
        else if (CalendarDay.today().isAfter(habitData.getBeginCalendarDay()))
            circularProgressIndicatorHabitDay.setCurrentProgress(DatesParser.countDifferenceBetweenDays(habitData.getBeginCalendarDay(), CalendarDay.today()) + 1);
        else if (CalendarDay.today().isBefore(habitData.getBeginCalendarDay()))
            circularProgressIndicatorHabitDay.setCurrentProgress(0);
        else
            circularProgressIndicatorHabitDay.setCurrentProgress(1);

        habitTextAdapter.setMaxProgress(circularProgressIndicatorHabitDay.getMaxProgress());
        circularProgressIndicatorHabitDay.setProgressTextAdapter(habitTextAdapter);

        if (circularProgressIndicatorHabitDay.getProgress() / circularProgressIndicatorHabitDay.getMaxProgress() <= 0.4)
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabitDay.getProgress() / circularProgressIndicatorHabitDay.getMaxProgress() <= 0.75)
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }

    /**
     * Counts the progression for the habit from the start date to today
     */
    private double countCurrentProgress(HabitData habitData) {
        LocalDate checkDate = LocalDate.now();
        LocalDate beginDate = DatesParser.toLocalDate(habitData.getBeginCalendarDay());

        if (CalendarDay.today().isAfter(habitData.getEndCalendarDay()))
            checkDate = DatesParser.toLocalDate(habitData.getEndCalendarDay());
        else if( LocalDate.now() == DatesParser.toLocalDate(habitData.getBeginCalendarDay()))
            if (habitData.isHabitDone(habitData.getBeginCalendarDay()))
                return 100D;
        else
            return 0.0;

        double days = 0;
        double doneHabitDays = 0;

        for (LocalDate i = beginDate; i.isBefore(checkDate) || i.isEqual(checkDate); i = i.plusDays(1)) {
            if (habitData.isDayOfWeekChecked(i)) {
                days++;
                if (habitData.isHabitDone(DatesParser.toCalendarDay(i)))
                    doneHabitDays++;
            }
        }
        return (doneHabitDays / days) * 100;
    }
}
