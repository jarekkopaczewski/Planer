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
import skills.future.planer.db.task.TaskData;
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
    private AtomicInteger positionToChange = new AtomicInteger(-1);


    public TaskTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case LAYOUT_SMALL -> {
                itemView = layoutInflater.inflate(R.layout.fragment_task_in_list, parent, false);

                AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
                itemView.setOnClickListener(e -> {
                    CheckBox checkBox = itemView.findViewById(R.id.checkBoxTask);
                    boolean isSelected = checkBox.isChecked();
                    checkBox.setChecked(!isSelected);
                    AnimateView.animateInOut(itemView.findViewById(R.id.taskCard), context);
                });
                return new TaskDataViewHolder(itemView, context);
            }
            case LAYOUT_BIG -> {
                itemView = layoutInflater.inflate(R.layout.fragment_task_in_list_extended, parent, false);
                itemView.findViewById(R.id.detailImageView).setOnClickListener(e ->
                        AnimateView.singleAnimation(itemView.findViewById(R.id.detailImageView), context, R.anim.rotate));

                AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);

                return new TaskDataViewHolderExtended(itemView, context);
            }
            default -> {
                return new TaskDataViewHolder(null, context);
            }
        }
    }

    public TaskData getTaskDataAtPosition(int position) {
        return filteredTaskList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDataViewHolder holder, int position) {
        if (filteredTaskList != null) {
            TaskData current = filteredTaskList.get(position);
            holder.setEveryThing(current);
        } else {
            // Covers the case of data not being ready yet.
            holder.getTitle().setText("No Word");
        }

        holder.itemView.setOnClickListener(v -> {
            if (positionToChange.get() == -1) {
                System.out.println(holder.getAdapterPosition());
                positionToChange.set(holder.getAdapterPosition());
                this.notifyItemChanged(holder.getAdapterPosition());
            } else {
                int p = positionToChange.get();
                positionToChange.set(-1);
                this.notifyItemChanged(p);
            }
        });

        holder.itemView.findViewById(R.id.detailImageView).setOnClickListener(e -> {
            Navigation.findNavController(holder.itemView)
                    .navigate(TaskListFragmentDirections
                            .navToEditTaskListCreatorFragment(fullTaskList.get(position).getTaskDataId()));
        });


    }

    @Override
    public int getItemViewType(int position) {
        if (position == positionToChange.get())
            return LAYOUT_BIG;
        return LAYOUT_SMALL;
    }

    @SuppressLint("NotifyDataSetChanged")
    void setFilteredTaskList(List<TaskData> words) {
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
