package skills.future.planer.ui.goals.pager.recycler;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;

public class GoalViewHolderTitle extends ICustomViewHolder {
    private final CircularProgressIndicator progressBar;
    private final TextView goalTitle;
    private final Context context;

    public GoalViewHolderTitle(View itemView, Context context, Fragment fragment) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.circularProgressIndicatorGoal);
        goalTitle = itemView.findViewById(R.id.goalTitleText);
        this.context = context;
        setSomething();
    }

    private void setSomething() {
        progressBar.setMaxProgress(100f);
        progressBar.setCurrentProgress(20f);
        //goalTitle.setText("Blablalblabalb");
    }


}
