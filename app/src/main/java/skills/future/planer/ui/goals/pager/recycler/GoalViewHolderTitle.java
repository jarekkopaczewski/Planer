package skills.future.planer.ui.goals.pager.recycler;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.day.views.habits.TextAdapter;

public class GoalViewHolderTitle extends ICustomViewHolder {
    private final CircularProgressIndicator progressBar;
    private final TextView goalTitle;
    private final Context context;
    private final TextView goalDate;
    private final SimpleDateFormat formatter =
            new SimpleDateFormat("LLLL yyyy", Locale.getDefault());

    public GoalViewHolderTitle(View itemView, Context context, ComponentActivity activity) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.circularProgressIndicatorGoal);
        goalTitle = itemView.findViewById(R.id.goalTitleText);
        this.context = context;
        this.goalDate = itemView.findViewById(R.id.goal_date);


        progressBar.setMaxProgress(100f);
        progressBar.setProgressTextAdapter(new TextAdapter());

        if (progressBar.getProgress() <= 40)
            progressBar.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (progressBar.getProgress() <= 75)
            progressBar.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            progressBar.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }


    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        GoalData goalData = (GoalData) element;
        goalTitle.setText(goalData.getTitle());
        //TODO podczepić liczenie progresu
        progressBar.setCurrentProgress(new Random().nextInt(100));
        this.goalDate.setText(formatter.format(DatesParser.toCalendar(goalData.getDateCalendarDate()).getTime()));
    }
}
