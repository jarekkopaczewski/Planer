package skills.future.planer.ui.goals.pager.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.view_holders.HabitExtendedViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class MixedViewAdapter extends RecyclerView.Adapter<ICustomViewHolder> {
    protected static final int LAYOUT_HABIT = 0;
    private static final int LAYOUT_TASK = 1;
    private final LayoutInflater layoutInflater;
    protected final Context context;
    protected final ComponentActivity activity;
    protected List<MixedRecyclerElement> habitsList = new ArrayList<>();
    protected List<TaskData> fullTaskList = new ArrayList<>();

    public MixedViewAdapter(Context context, ComponentActivity activity) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < habitsList.size()) return LAYOUT_HABIT;
        else return LAYOUT_TASK;
    }

    @Override
    public int getItemCount() {
        return fullTaskList.size() + habitsList.size();
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @NonNull
    @Override
    public ICustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_HABIT -> new HabitExtendedViewHolder(createViewOfItem(parent,
                    R.layout.fragment_habit_in_list_extended), context, activity);
            default -> new TaskDataViewHolder(createViewOfItem(parent, R.layout.fragment_task_in_list),
                    context, activity);
        };
    }

    @NonNull
    protected View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @Override
    public void onBindViewHolder(ICustomViewHolder holder, final int position) {
        if (holder.getItemViewType() == LAYOUT_HABIT) {
            holder.setEveryThing(habitsList.get(position));
        } else {
            holder.setEveryThing(fullTaskList.get(position - habitsList.size()));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHabitsList(List<MixedRecyclerElement> habitsList) {
        this.habitsList = habitsList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFullTaskList(List<TaskData> fullTaskList) {
        this.fullTaskList = fullTaskList;
        notifyDataSetChanged();
    }
}
