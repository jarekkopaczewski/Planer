package skills.future.planer.ui.tasklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;
import skills.future.planer.ui.tasklist.filters_chain.BaseFilter;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolderExtended;

public class TaskTotalAdapter extends RecyclerView.Adapter<TaskDataViewHolder> implements Filterable {
    private final LayoutInflater layoutInflater;
    protected final Context context;
    private final ComponentActivity activity;
    private BaseFilter filter;
    private List<TaskData> displayedList = new ArrayList<>(), fullList = new ArrayList<>(), filteredList = new ArrayList<>();
    private static final int LAYOUT_SMALL = 0;
    private static final int LAYOUT_BIG = 1;
    private final AtomicInteger positionToChange = new AtomicInteger(-1);
    private TaskFilter textFilter = null;

    public TaskTotalAdapter(Context context, ComponentActivity activity, BaseFilter filter) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
        this.filter = filter;
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //noinspection SwitchStatementWithTooFewBranches
        return switch (viewType) {
            case LAYOUT_BIG -> new TaskDataViewHolderExtended(
                    createViewOfItem(parent,
                            R.layout.fragment_task_in_list_extended), context, activity);
            default -> new TaskDataViewHolder(
                    createViewOfItem(parent,
                            R.layout.fragment_task_in_list), context, activity);
        };
    }

    @NonNull
    protected View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDataViewHolder holder, int position) {
        if (displayedList != null) {
            TaskData current = displayedList.get(position);
            holder.setEveryThing((MixedRecyclerElement) current);
        }
        createListenerToExtendView(holder);
    }

    /**
     * Creates listener to taskView in list
     * When someone presses on a taskView it will expand or close
     */
    protected void createListenerToExtendView(@NonNull TaskDataViewHolder holder) {
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

    @Override
    public int getItemViewType(int position) {
        if (position == positionToChange.get())
            return LAYOUT_BIG;
        return LAYOUT_SMALL;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDisplayedList(List<TaskData> words) {
        fullList = words;
        CategoryFilter();
    }

    @Override
    public int getItemCount() {
        if (displayedList != null)
            return displayedList.size();
        else return 0;
    }

    /**
     * Filter for task's category
     */
    @SuppressLint("NotifyDataSetChanged")
    public void CategoryFilter() {
        filteredList.clear();
        filteredList.addAll(filter != null ? filter.filter(fullList) : fullList);
        notifyDataSetChanged();
    }

    /**
     * Creates new filter
     *
     * @return new Task filter
     */
    @Override
    public Filter getFilter() {
        if (textFilter == null)
            textFilter = new TaskFilter();
        return textFilter;
    }

    public void addFilter(BaseFilter taskDoneFilter) {
        if (filter == null)
            filter = taskDoneFilter;
        else
            filter.setNext(taskDoneFilter);
    }

    public void cleanFilters() {
        filter = null;
    }

    /**
     * Filter class
     * Searches for string match in task titles
     */
    private class TaskFilter extends Filter {
        private final List<TaskData> filteredItems = new ArrayList<>();

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // CategoryFilter(filters);
            filteredItems.clear();
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {

                for (int i = 0, l = filteredList.size(); i < l; i++) {
                    TaskData taskData = filteredList.get(i);
                    if (taskData.getTaskTitleText().toLowerCase().contains(constraint))
                        filteredItems.add(taskData);
                }
            }
            return result;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void publishResults(CharSequence constraint, FilterResults results) {

            displayedList.clear();
            if (constraint.toString().length() > 0) {
                displayedList.addAll((ArrayList<TaskData>) filteredItems);

            } else {
                displayedList.addAll((ArrayList<TaskData>) filteredList);
            }
            notifyDataSetChanged();
        }
    }
}
