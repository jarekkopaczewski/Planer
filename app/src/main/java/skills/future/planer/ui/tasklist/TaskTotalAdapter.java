package skills.future.planer.ui.tasklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import skills.future.planer.R;
import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolderExtended;

public class TaskTotalAdapter extends RecyclerView.Adapter<TaskDataViewHolder> implements Filterable {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<TaskData> filteredTaskList = new ArrayList<>();
    private List<TaskData> fullTaskList = new ArrayList<>();
    private static final int LAYOUT_SMALL = 0;
    private static final int LAYOUT_BIG = 1;
    private final AtomicInteger positionToChange = new AtomicInteger(-1);


    public TaskTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_SMALL -> new TaskDataViewHolder(
                    createViewOfItem(parent,
                            R.layout.fragment_task_in_list), context);
            case LAYOUT_BIG -> new TaskDataViewHolderExtended(
                    createViewOfItem(parent,
                            R.layout.fragment_task_in_list_extended), context);
            default -> new TaskDataViewHolder(null, context);
        };
    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        itemView.setOnClickListener(e -> {
            CheckBox checkBox = itemView.findViewById(R.id.checkBoxTask);
            checkBox.setChecked(!checkBox.isChecked());
        });
        return itemView;
    }

    public TaskData getTaskDataAtPosition(int position) {
        return filteredTaskList.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskDataViewHolder holder, int position) {
        if (filteredTaskList != null) {
            TaskData current = filteredTaskList.get(position);
            holder.setEveryThing(current);
        } else // Covers the case of data not being ready yet.
            holder.getTitle().setText("No Word");

        createListenerToExtendView(holder);
        createListenerToEditButton(holder, position);


    }

    /**
     * Creates listener to taskView in list
     * When someone presses on a taskView it will expand or close
     */
    private void createListenerToExtendView(@NonNull TaskDataViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {

            int positionAtomic = positionToChange.get();

            // if no window is open
            if (positionAtomic == -1) {
                positionToChange.set(holder.getAdapterPosition());
                this.notifyItemChanged(holder.getAdapterPosition());
            } else {

                int adapterPosition = holder.getAdapterPosition();
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

    /**
     * Creates listener to edit button which starts a TaskListCreatorFragment
     */
    private void createListenerToEditButton(@NonNull TaskDataViewHolder holder, int position) {
        holder.itemView.findViewById(R.id.detailImageView).setOnClickListener(e ->
                Navigation.findNavController(holder.itemView)
                        .navigate(TaskListFragmentDirections
                                .navToEditTaskListCreatorFragment(fullTaskList.get(position).getTaskDataId())));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == positionToChange.get())
            return LAYOUT_BIG;
        return LAYOUT_SMALL;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredTaskList(List<TaskData> words) {
        filteredTaskList = words;
        fullTaskList = new ArrayList<>(filteredTaskList);
        notifyDataSetChanged();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (filteredTaskList != null)
            return filteredTaskList.size();
        else return 0;
    }

    /**
     * Filter for task's category
     *
     * @param filters list of checked filters names
     * @throws Exception exception
     */
    @SuppressLint("NotifyDataSetChanged")
    public void CategoryFilter(ArrayList<String> filters) throws Exception {

        TaskCategory category = null;
        Priorities priorities = null;
        TimePriority timePriority = null;

        //checking which filter is checked
        for (String filter : filters) {
            if (filter.equals(TaskCategory.Private.toString())) {
                category = TaskCategory.Private;
            }
            if (filter.equals(TaskCategory.Work.toString())) {
                category = TaskCategory.Work;
            }
            if (filter.equals(TimePriority.Urgent.toString())) {
                timePriority = TimePriority.Urgent;
            }
            if (filter.equals(TimePriority.NotUrgent.toString())) {
                timePriority = TimePriority.NotUrgent;
            }
            if (filter.equals(Priorities.Important.toString())) {
                priorities = Priorities.Important;
            }
            if (filter.equals(Priorities.NotImportant.toString())) {
                priorities = Priorities.NotImportant;
            }
        }

        //list of filtered tasks
        List<TaskData> list = new ArrayList<>();

        //checks which query to execute
        if (category == null && priorities == null && timePriority != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(timePriority);
        }
        if (category == null && timePriority == null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities);
        }
        if (timePriority == null && priorities == null && category != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(category);
        }
        if (category == null && timePriority != null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities, timePriority);
        }
        if (timePriority == null && category != null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities, category);
        }
        if (priorities == null && timePriority != null && category != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(category, timePriority);
        }
        if (category != null && timePriority != null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities, timePriority, category);
        }
        if (category == null && priorities == null && timePriority == null) {
            list = fullTaskList;
        }

        filteredTaskList = list;
        notifyDataSetChanged();

    }

    /**
     * Creates new filter
     *
     * @return new Task filter
     */
    @Override
    public Filter getFilter() {
        return new TaskFilter();
    }

    //todo Trzeba zrobić synchronizacje po filtrze jezeli będą zmiany

    /**
     * Filter class
     * Searches for string match in task titles
     */
    private class TaskFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<TaskData> filteredItems = new ArrayList<>();

                for (int i = 0, l = filteredTaskList.size(); i < l; i++) {
                    TaskData taskData = filteredTaskList.get(i);
                    if (taskData.getTaskTitleText().toLowerCase().contains(constraint))
                        filteredItems.add(taskData);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = fullTaskList;
                }
            }
            return result;
        }

        @SuppressLint("NotifyDataSetChanged")
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredTaskList.clear();
            filteredTaskList.addAll((ArrayList<TaskData>) results.values);
            notifyDataSetChanged();
        }
    }

}
