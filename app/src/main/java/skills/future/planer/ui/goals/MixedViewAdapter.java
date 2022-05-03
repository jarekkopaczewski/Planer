package skills.future.planer.ui.goals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.views.habits.HabitViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class MixedViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LAYOUT_HABIT = 0;
    private static final int LAYOUT_TASK = 1;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final List<String> habitsList = new ArrayList<>(Arrays.asList("Nawyk testowy", "Nawyk testowy 2", "Nawyk nawyk nawyk nawyk", "Nawyk testowy 2", "Nawyk testowy 2"));
    private final List<TaskData> fullTaskList = new ArrayList<>();

    public MixedViewAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_HABIT -> new HabitViewHolder(createViewOfItem(parent, R.layout.fragment_habit_in_list_extended), context);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == LAYOUT_HABIT) {

        } else {
            //TaskData current = fullTaskList.get(position);
            //(TaskDataViewHolder)holder.setEveryThing(current);
        }
    }
}
