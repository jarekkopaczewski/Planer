package skills.future.planer.ui.goals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akki.circlemenu.CircleMenu;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.HabitCreatorActivity;

@Setter
public class GoalTotalAdapter extends RecyclerView.Adapter<GoalViewHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final HabitViewModel habitViewModel;
    private final GoalsViewModel goalsViewModel;
    private Lifecycle lifecycle;
    private FragmentManager fragmentManager;
    private List<GoalData> goalsList = new ArrayList<>();
    private MixedViewAdapter mixedViewAdapter;

    public GoalTotalAdapter(Context context, GoalsFragment lifecycleOwner,
                            HabitViewModel habitViewModel, GoalsViewModel goalsViewModel) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.habitViewModel = habitViewModel;
        this.goalsViewModel = goalsViewModel;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoalViewHolder(createViewOfItem(parent, R.layout.goal_in_list), context, fragmentManager);
    }

    @SuppressLint("NonConstantResourceId")
    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);

        mixedViewAdapter = new MixedViewAdapter(context, habitViewModel,
                lifecycleOwner, goalsViewModel);
        RecyclerView list = itemView.findViewById(R.id.goalsList);
        list.setAdapter(mixedViewAdapter);
        list.setLayoutManager(new LinearLayoutManager(context));

        CircleMenu circleMenu = itemView.findViewById(R.id.circle_menu);

        circleMenu.setOnMenuItemClickListener(id -> {
            switch (id) {
                case R.drawable.routine -> context.startActivity(
                        new Intent(context, HabitCreatorActivity.class));
                case R.drawable.task_list -> Navigation.findNavController(itemView)
                        .navigate(GoalsFragmentDirections
                                .actionNavHomeToTaskListCreatorFragment(-1));
            }
        });

        return itemView;
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        if (goalsList != null) {
            GoalData current = goalsList.get(position);
            holder.setEveryThing(current);
            goalsViewModel.getHabitsFromGoal(goalsList.get(position).getGoalId())
                    .observe(lifecycleOwner, goalDataListMap -> {
                        if (goalDataListMap.size() > 0)
                            mixedViewAdapter.setHabitsList(new ArrayList<>(goalDataListMap.values()));
                    });
            goalsViewModel.getTasksFromGoal(goalsList.get(position).getGoalId())
                    .observe(lifecycleOwner, goalDataListMap -> {
                        if (goalDataListMap.size() > 0)
                            mixedViewAdapter.setFullTaskList(new ArrayList<>(goalDataListMap.values()));
                    });
        } else // Covers the case of data not being ready yet.
            holder.getTitle().setText("No Word");
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (goalsList != null)
            return goalsList.size();
        else return 0;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setGoalsList(List<GoalData> goalsList) {
        this.goalsList = goalsList;
        notifyDataSetChanged();
    }
}
