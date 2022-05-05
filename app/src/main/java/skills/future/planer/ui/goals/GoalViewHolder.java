package skills.future.planer.ui.goals;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.ui.day.views.habits.TextAdapter;

@Getter
public class GoalViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final CircularProgressIndicator circularProgressIndicatorHabit;
    private final Context context;
    private final FragmentManager fragmentManager;


    public GoalViewHolder(View itemView, Context context, FragmentManager fragmentManager) {
        super(itemView);
        title = itemView.findViewById(R.id.goalTitleText);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorGoal);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public void setEveryThing(String title) {
        this.title.setText(title);
        Random random = new Random();
        circularProgressIndicatorHabit.setCurrentProgress(random.nextInt(99));
        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());

        if (circularProgressIndicatorHabit.getProgress() <= 40)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color. bad));
        else if (circularProgressIndicatorHabit.getProgress() <= 75)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }
}