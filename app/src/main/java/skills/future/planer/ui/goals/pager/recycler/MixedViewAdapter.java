package skills.future.planer.ui.goals.pager.recycler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.goals.creator.GoalsCreatorActivity;
import skills.future.planer.ui.habit.view_holders.HabitExtendedViewHolder;
import skills.future.planer.ui.habit.view_holders.HabitSimpleViewHolder;
import skills.future.planer.ui.habit.view_holders.HabitViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolderExtended;

public class MixedViewAdapter extends RecyclerView.Adapter<ICustomViewHolder> {
    protected static final int LAYOUT_HABIT = 2;
    private static final int LAYOUT_DESCRIPTION = 1;
    protected static final int LAYOUT_HABIT_EXTENDED = 4;
    private static final int LAYOUT_TASK = 3;
    private static final int LAYOUT_TITLE = 0;
    private static final int LAYOUT_TASK_EXTENDED = 5;
    private final LayoutInflater layoutInflater;
    protected final Context context;
    protected final ComponentActivity activity;
    protected List<MixedRecyclerElement> habitsList = new ArrayList<>();
    protected List<MixedRecyclerElement> fullTaskList = new ArrayList<>();
    private GoalData goalData;
    protected final AtomicInteger positionToChange = new AtomicInteger(-1);

    public MixedViewAdapter(Context context, ComponentActivity activity) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return switch (position) {
            case 0 -> LAYOUT_TITLE;
            case 1 -> LAYOUT_DESCRIPTION;
            default -> checkOrder(getPositionDelay(position));
        };
    }

    protected int checkOrder(int position) {
        if (position < habitsList.size())
            if (position == positionToChange.get() - 2)
                return LAYOUT_HABIT_EXTENDED;
            else
                return LAYOUT_HABIT;
        else if (position == positionToChange.get() - 2)
            return LAYOUT_TASK_EXTENDED;
        else
            return LAYOUT_TASK;
    }

    protected int getPositionDelay(int position) {
        return position - 2;
    }

    @Override
    public int getItemCount() {
        return habitsList.size() + fullTaskList.size() + 2;
    }

    @NonNull
    @Override
    public ICustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_TITLE -> new GoalViewHolderTitle(createViewOfItem(parent,
                    R.layout.goal_in_list_title), context, activity);
            case LAYOUT_DESCRIPTION -> new GoalViewHolderDescription(createViewOfItem(parent,
                    R.layout.goal_in_list_description), context, activity);
            case LAYOUT_HABIT -> new HabitSimpleViewHolder(createViewOfItem(parent,
                    R.layout.fragment_habit_in_list_simple), context, activity);
            case LAYOUT_HABIT_EXTENDED -> new HabitExtendedViewHolder(createViewOfItem(parent,
                    R.layout.fragment_habit_in_list_extended), context, activity);
            case LAYOUT_TASK_EXTENDED -> new TaskDataViewHolderExtended(createViewOfItem(parent,
                    R.layout.fragment_task_in_list_extended), context, activity);
            default -> new TaskDataViewHolder(createViewOfItem(parent,
                    R.layout.fragment_task_in_list), context, activity);
        };
    }

    @NonNull
    protected View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @SneakyThrows
    @Override
    public void onBindViewHolder(ICustomViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case LAYOUT_TITLE -> {
                createListenerToEditButton(holder);
                createListenerToTrashButton(holder);
                holder.setEveryThing(goalData);
            }
            case LAYOUT_DESCRIPTION -> holder.setEveryThing(goalData);
            case LAYOUT_HABIT, LAYOUT_HABIT_EXTENDED -> {
                holder.setEveryThing(habitsList.get(getPositionDelay(position)));
                createListenerToExtendView(holder);
            }
            case LAYOUT_TASK, LAYOUT_TASK_EXTENDED -> {
                holder.setEveryThing(fullTaskList.get(getPositionDelay(position) - habitsList.size()));
                createListenerToExtendView(holder);
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setGoalData(GoalData goalData) {
        this.goalData = goalData;
        notifyDataSetChanged();
    }

    private void createListenerToEditButton(@NonNull ICustomViewHolder holder) {
        holder.itemView.findViewById(R.id.editImageBtn).setOnClickListener(e -> {
            var intent = new Intent(context, GoalsCreatorActivity.class);
            var bundle = new Bundle();
            bundle.putLong("goalIdToEdit", goalData.getGoalId());
            intent.putExtras(bundle);
            context.startActivity(intent);
            notifyItemChanged(0);
        });
    }

    /**
     * Creates listener to taskView in list
     * When someone presses on a taskView it will expand or close
     */
    protected void createListenerToExtendView(@NonNull ICustomViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {

            int positionAtomic = positionToChange.get();

            // if no window is open
            if (positionAtomic == -1) {
                positionToChange.set(holder.getBindingAdapterPosition());
                this.notifyItemChanged(holder.getBindingAdapterPosition());
            } else {
                int adapterPosition = holder.getBindingAdapterPosition();
                this.notifyItemChanged(positionAtomic);

                //if the window is open but we choose another
                if (positionAtomic != adapterPosition) {
                    positionToChange.set(adapterPosition);
                    this.notifyItemChanged(positionAtomic);
                } else
                    positionToChange.set(-1);

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void createListenerToTrashButton(@NonNull ICustomViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Potwierdź usunięcie");
        builder.setMessage("Jesteś pewien, że chcesz usunać?");

        builder.setPositiveButton("Usuń", (dialog, which) -> {
            var view = new ViewModelProvider(activity).get(GoalsViewModel.class);

            new ViewModelProvider(activity).get(GoalsViewModel.class).delete(goalData);
            dialog.dismiss();
        });

        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        holder.itemView.findViewById(R.id.trashImageView).setOnClickListener(e -> alert.show());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHabitsList(List<MixedRecyclerElement> habitsList) {
        this.habitsList = habitsList;
        this.habitsList.sort(Comparator.comparingLong(h -> ((HabitData) h).getEndDay()));
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFullTaskList(List<MixedRecyclerElement> fullTaskList) {
        this.fullTaskList = fullTaskList;
        this.fullTaskList.sort(Comparator.comparingLong(t -> ((TaskData) t).getEndingDate()));
        notifyDataSetChanged();
    }

}
