package skills.future.planer.ui.habit.view_holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;
import skills.future.planer.ui.habit.HabitTextAdapter;

@Getter
public class HabitSimpleViewHolder extends HabitViewHolder {

    private final CircularProgressIndicator circularProgressIndicatorHabitDay;
    private final TextView habitTitleTextViewSimple;
    private final Context context;


    public HabitSimpleViewHolder(View itemView, Context context, ComponentActivity activity) {
        super(itemView, context);
        circularProgressIndicatorHabitDay = itemView.findViewById(R.id.circularProgressIndicatorHabitSimple);
        habitTitleTextViewSimple = itemView.findViewById(R.id.habitTitleTextViewSimple);
        this.context = context;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        if (element instanceof HabitData habitData) {
            setUpCircularProgressIndicatorOfDays(habitData);
            habitTitleTextViewSimple.setText(habitData.getTitle());
        }
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
