package skills.future.planer.ui.day.views.matrix;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;

public class MatrixTotalAdapter extends RecyclerView.Adapter<MatrixTotalAdapter.TaskDataViewHolder> implements Filterable {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<TaskData> filteredTaskList = new ArrayList<>();
    private List<TaskData> fullTaskList = new ArrayList<>();
    private final Priorities priorities;
    private final TimePriority timePriority;

    public MatrixTotalAdapter(Context context, Priorities priorities, TimePriority timePriority) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.priorities = priorities;
        this.timePriority = timePriority;
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(R.layout.fragment_task_in_matrix_done, parent, false);

        itemView.setOnClickListener(e -> {
            AnimateView.animateInOut(itemView.findViewById(R.id.taskCard), context);
        });

        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return new TaskDataViewHolder(itemView);
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
            holder.title.setText("No Word");
        }
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
     * Creates new filter
     *
     * @return new Task filter
     */
    @Override
    public Filter getFilter() {
        return new MatrixFilter();
    }

    //todo Trzeba zrobić synchronizacje po filtrze jezeli będą zmiany

    /**
     * Filter class
     * Searches for string match in task titles
     */
    private class MatrixFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            ArrayList<TaskData> filteredItems = new ArrayList<>();

            for( TaskData item : fullTaskList)
            {
                if (item.getPriorities() == priorities && item.getTimePriority() == timePriority)
                    filteredItems.add(item);
            }

            result.values = filteredItems;
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

    class TaskDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView done;

        private TaskDataViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitleTextView);
            done = itemView.findViewById(R.id.doneImageView);
        }

        public void setEveryThing(TaskData taskData) {
            setTextTitle(taskData);
            check(taskData);
        }

        private void setTextTitle(TaskData task) {
            String titleText = task.getTaskTitleText();
            title.setText(titleText);
        }

        private void check(TaskData task) {
            if (task.getStatus() == true)
                done.setVisibility(VISIBLE);
        }

    }
}
