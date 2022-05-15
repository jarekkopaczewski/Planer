package skills.future.planer.ui.goals.pager;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.ui.goals.pager.recycler.MixedViewAdapter;

@Getter
public class GoalViewHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final GoalsViewModel goalsViewModel;
    private final Fragment fragment;
    private MixedViewAdapter mixedViewAdapter;

    public GoalViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        this.context = fragment.getContext();
        this.goalsViewModel = new ViewModelProvider(fragment).get(GoalsViewModel.class);
        this.fragment = fragment;
        mixedViewAdapter = new MixedViewAdapter(context, fragment);
        RecyclerView list = itemView.findViewById(R.id.goalsList);
        list.setAdapter(mixedViewAdapter);
        list.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setEveryThing(GoalData goalData) {

        goalsViewModel.getHabitsFromGoal(goalData.getGoalId())
                .observe(fragment, goalDataListMap -> {
                    if (goalDataListMap.size() > 0)
                        mixedViewAdapter.setHabitsList(goalData, new ArrayList<>(goalDataListMap.values()));
                });
        goalsViewModel.getTasksFromGoal(goalData.getGoalId())
                .observe(fragment, goalDataListMap -> {
                    if (goalDataListMap.size() > 0)
                        mixedViewAdapter.setFullTaskList(goalData, new ArrayList<>(goalDataListMap.values()));
                });
    }
}