package skills.future.planer.ui.goals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.HabitExtendedViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class MixedViewAdapter extends RecyclerView.Adapter<ICustomViewHolder> {

    private static final int LAYOUT_HABIT = 0;
    private static final int LAYOUT_TASK = 1;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final List<HabitData> habitsList = new ArrayList<>();
    private final List<TaskData> fullTaskList = new ArrayList<>();
    private final HabitViewModel habitViewModel;
    private final LifecycleOwner lifecycleOwner;

    public MixedViewAdapter(Context context, HabitViewModel habitViewModel, LifecycleOwner lifecycleOwner) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.habitViewModel = habitViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return LAYOUT_HABIT;
        else return LAYOUT_TASK;
    }

    @Override
    public int getItemCount() {
        return fullTaskList.size() + habitsList.size();
    }

    @NonNull
    @Override
    public ICustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_HABIT -> new HabitExtendedViewHolder(createViewOfItem(parent, R.layout.fragment_habit_in_list_extended), context, habitViewModel, lifecycleOwner);
            default -> new TaskDataViewHolder(createViewOfItem(parent, R.layout.fragment_task_in_list), context);
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
            holder.setEveryThing(fullTaskList.get(position));
        }
    }
}
