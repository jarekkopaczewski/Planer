package skills.future.planer.ui.goals.pager.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.HabitExtendedViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class MixedViewAdapter extends RecyclerView.Adapter<ICustomViewHolder> {
    private static final int LAYOUT_TITLE = 0;
    private static final int LAYOUT_DESCRIPTION = 1;
    private static final int LAYOUT_HABIT = 2;
    private static final int LAYOUT_TASK = 3;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final Fragment fragment;
    private ArrayList<Integer> habitOrTaskList = new ArrayList<>();
    private List<MixedRecyclerElement> habitsList = new ArrayList<>();
    private List<MixedRecyclerElement> fullTaskList = new ArrayList<>();
    private GoalData goalData;

    public MixedViewAdapter(Context context, Fragment fragment) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return LAYOUT_TITLE;
        if (position == 1) return LAYOUT_DESCRIPTION;
        if (position < habitsList.size()) return LAYOUT_HABIT;
        else return LAYOUT_TASK;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @NonNull
    @Override
    public ICustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_TITLE -> new GoalViewHolderTitle(createViewOfItem(parent, R.layout.goal_in_list_title), context, fragment);
            case LAYOUT_DESCRIPTION -> new GoalViewHolderDescription(createViewOfItem(parent, R.layout.goal_in_list_description), context, fragment);
            case LAYOUT_HABIT -> new HabitExtendedViewHolder(createViewOfItem(parent, R.layout.fragment_habit_in_list_extended), context, fragment);
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
        switch (holder.getItemViewType()) {
            case LAYOUT_TITLE -> ((GoalViewHolderTitle) holder).setEveryThing(goalData);
            case LAYOUT_DESCRIPTION -> ((GoalViewHolderDescription) holder).setEveryThing(goalData);
            case LAYOUT_HABIT -> holder.setEveryThing(habitsList.get(position));
            // case LAYOUT_TASK -> holder.setEveryThing(fullTaskList.get(position));
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setHabitsList(GoalData goalData, ArrayList<MixedRecyclerElement> habitsList) {
        this.habitsList = habitsList;
        this.goalData = goalData;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFullTaskList(GoalData goalData, ArrayList<MixedRecyclerElement> fullTaskList) {
        this.fullTaskList = fullTaskList;
        this.goalData = goalData;
        notifyDataSetChanged();
    }
}
