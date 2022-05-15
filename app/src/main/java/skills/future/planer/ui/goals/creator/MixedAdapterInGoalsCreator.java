package skills.future.planer.ui.goals.creator;

import android.content.Context;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;

import skills.future.planer.R;
import skills.future.planer.ui.goals.pager.recycler.ICustomViewHolder;
import skills.future.planer.ui.goals.pager.recycler.MixedViewAdapter;
import skills.future.planer.ui.habit.view_holders.HabitExtendedViewHolder;

public class MixedAdapterInGoalsCreator extends MixedViewAdapter {
    public MixedAdapterInGoalsCreator(Context context, ComponentActivity activity) {
        super(context, activity);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @NonNull
    @Override
    public ICustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_HABIT -> new HabitExtendedViewHolder(createViewOfItem(parent,
                    R.layout.fragment_habit_in_day_list), context, activity);
            default -> new TaskViewHolderGoalsCreator(createViewOfItem(parent,
                    R.layout.fragment_task_in_list), context, activity);
        };
    }

    @Override
    public void onBindViewHolder(ICustomViewHolder holder, final int position) {
        if (holder.getItemViewType() == LAYOUT_HABIT) {
            holder.setEveryThing(habitsList.get(position));
        } else {
            holder.setEveryThing(fullTaskList.get(position - habitsList.size()));
        }
    }
}
