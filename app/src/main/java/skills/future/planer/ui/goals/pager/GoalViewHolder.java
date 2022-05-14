package skills.future.planer.ui.goals.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.ui.day.views.habits.TextAdapter;
import skills.future.planer.ui.goals.pager.recycler.MixedViewAdapter;

@Getter
public class GoalViewHolder extends RecyclerView.ViewHolder {
    private final TextView title, details;
    private final CircularProgressIndicator circularProgressIndicatorHabit;
    private final Context context;
    private final GoalsViewModel goalsViewModel;
    private final Fragment fragment;
    private MixedViewAdapter mixedViewAdapter;

    public GoalViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        title = itemView.findViewById(R.id.goalTitleText);
        details = itemView.findViewById(R.id.text_slideshow2);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorGoal);
        this.context = fragment.getContext();
        this.goalsViewModel = new ViewModelProvider(fragment).get(GoalsViewModel.class);
        this.fragment = fragment;
        mixedViewAdapter = new MixedViewAdapter(context, fragment);
        RecyclerView list = itemView.findViewById(R.id.goalsList);
        list.setAdapter(mixedViewAdapter);
        list.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setEveryThing(GoalData goalData) {
        this.title.setText(goalData.getTitle());
        this.details.setText(goalData.getDetails());
        Random random = new Random();
        circularProgressIndicatorHabit.setCurrentProgress(random.nextInt(99));
        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());

        if (circularProgressIndicatorHabit.getProgress() <= 40)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabit.getProgress() <= 75)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.good));
        goalsViewModel.getHabitsFromGoal(goalData.getGoalId())
                .observe(fragment, goalDataListMap -> {
                    if (goalDataListMap.size() > 0)
                        mixedViewAdapter.setHabitsList(new ArrayList<>(goalDataListMap.values()));
                });
        goalsViewModel.getTasksFromGoal(goalData.getGoalId())
                .observe(fragment, goalDataListMap -> {
                    if (goalDataListMap.size() > 0)
                        mixedViewAdapter.setFullTaskList(new ArrayList<>(goalDataListMap.values()));
                });
    }
}