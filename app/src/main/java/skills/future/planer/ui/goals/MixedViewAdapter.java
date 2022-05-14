package skills.future.planer.ui.goals;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.SneakyThrows;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitDuration;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.HabitExtendedViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class MixedViewAdapter extends RecyclerView.Adapter<ICustomViewHolder> {

    private static final int LAYOUT_HABIT = 0;
    private static final int LAYOUT_TASK = 1;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<HabitData> habitsList = new ArrayList<>();
    private List<TaskData> fullTaskList = new ArrayList<>();
    private final HabitViewModel habitViewModel;
    private final LifecycleOwner lifecycleOwner;

    public MixedViewAdapter(Context context, HabitViewModel habitViewModel, LifecycleOwner lifecycleOwner) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.habitViewModel = habitViewModel;
        this.lifecycleOwner = lifecycleOwner;

        // test
        CalendarDay day2 = CalendarDay.from(2022, 12, 21);
        CalendarDay day3 = CalendarDay.from(2022, 12, 23);
        TaskData word = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent, "Zadanie ważne i pilne", "", day2, day3);
        fullTaskList = Arrays.asList(word, word, word);
        try {
            habitsList = Collections.singletonList(new HabitData("test", "1110011", HabitDuration.Short, DatesParser.toLocalDate(day2), 12, 0, 2L));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @SneakyThrows
    @Override
    public void onBindViewHolder(ICustomViewHolder holder, final int position) {

        if (holder.getItemViewType() == LAYOUT_HABIT) {
            holder.setEveryThing(habitsList.get(position));
        } else {
            holder.setEveryThing(fullTaskList.get(position-habitsList.size()));
        }
    }
}
