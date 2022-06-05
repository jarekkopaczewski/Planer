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

    private final TextView habitTitleTextViewSimple;
    private final Context context;


    public HabitSimpleViewHolder(View itemView, Context context, ComponentActivity activity) {
        super(itemView, context, activity);
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

}
