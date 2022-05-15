package skills.future.planer.ui.goals.pager.recycler;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.ui.day.views.habits.TextAdapter;

public class GoalViewHolderTitle extends ICustomViewHolder {
    private final CircularProgressIndicator progressBar;
    private final TextView goalTitle;
    private final Context context;

    public GoalViewHolderTitle(View itemView, Context context, Fragment fragment) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.circularProgressIndicatorGoal);
        goalTitle = itemView.findViewById(R.id.goalTitleText);
        this.context = context;

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
    }
}
