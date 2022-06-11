package skills.future.planer.ui.day.views.habits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.views.habits.viewHolder.HabitViewHolder;
import skills.future.planer.ui.day.views.habits.viewHolder.HabitViewHolderProgressBar;
import skills.future.planer.ui.day.views.habits.viewHolder.ICustomHabitDayViewHolder;

public class HabitTotalAdapter extends RecyclerView.Adapter<ICustomHabitDayViewHolder> {
    private static final int LAYOUT_PROGRESS = 0;
    private static final int LAYOUT_HABIT = 1;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final HabitDayViewModel habitDayViewModel;
    private List<HabitData> habitsList = new ArrayList<>();
    private final HabitViewModel habitViewModel;

    public HabitTotalAdapter(Context context, HabitViewModel habitViewModel, HabitDayViewModel habitDayViewModel) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.habitViewModel = habitViewModel;
        this.habitDayViewModel = habitDayViewModel;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHabitsList(List<HabitData> habitsList) {
        this.habitsList = habitsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ICustomHabitDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_PROGRESS -> new HabitViewHolderProgressBar(
                    createViewOfItem(parent, R.layout.fragment_habit_progress_in_day_list),
                    context, habitDayViewModel);
            case LAYOUT_HABIT -> new HabitViewHolder(
                    createViewOfItem(parent, R.layout.fragment_habit_in_day_list),
                    habitViewModel, this, context);
            default -> throw new IllegalStateException("Unexpected value: " + viewType);
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ICustomHabitDayViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case LAYOUT_PROGRESS -> {
            }
            //holder.setEveryThing();
            case LAYOUT_HABIT -> {
                if (habitsList != null) {
                    var current = habitsList.get(position);
                    holder.setEveryThing(current, position);
                }
            }
        }

    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < habitsList.size())
            return LAYOUT_HABIT;
        else
            return LAYOUT_PROGRESS;
    }

    /**
     * Gets number of habits in list
     *
     * @return if habitList isn't null size of list
     */
    @Override
    public int getItemCount() {
        if (habitsList != null)
            return habitsList.size() + 1;
        else return 0;
    }
}
