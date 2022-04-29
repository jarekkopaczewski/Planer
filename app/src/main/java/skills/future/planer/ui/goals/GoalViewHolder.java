package skills.future.planer.ui.goals;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.ui.day.views.habits.TextAdapter;

@Getter
public class GoalViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final CircularProgressIndicator circularProgressIndicatorHabit;


    public GoalViewHolder(View itemView, Context context) {
        super(itemView);
        title = itemView.findViewById(R.id.goalTitleText);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorGoal);
    }

    public void setEveryThing(String title) {
        this.title.setText(title);
        Random random = new Random();
        circularProgressIndicatorHabit.setCurrentProgress(random.nextInt(99));
        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());
    }
}