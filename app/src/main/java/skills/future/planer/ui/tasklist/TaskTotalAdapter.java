package skills.future.planer.ui.tasklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;

public class TaskTotalAdapter extends RecyclerView.Adapter<TaskTotalAdapter.TaskDataViewHolder> implements Filterable {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<TaskData> filteredTaskList = new ArrayList<>();
    private List<TaskData> fullTaskList = new ArrayList<>();

    public TaskTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.fragment_task_in_list, parent, false);

        itemView.setOnClickListener(e -> {
            CheckBox checkBox = itemView.findViewById(R.id.checkBoxTask);
            boolean isSelected = checkBox.isChecked();
            checkBox.setChecked(!isSelected);
            AnimateView.animateInOut(itemView.findViewById(R.id.taskCard), context);
        });

        itemView.findViewById(R.id.detailImageView).setOnClickListener(e ->
                AnimateView.singleAnimation(itemView.findViewById(R.id.detailImageView), context, R.anim.rotate));

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
            // Covers the case of data not being ready yet.
            holder.title.setText("No Word");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredTaskList(List<TaskData> words){
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
            filteredTaskList.addAll ((ArrayList<TaskData>) results.values);
            notifyDataSetChanged();
        }
    }

    class TaskDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, date;
        private final CheckBox checkBox;
        private final ImageView imageView, detailImageView;
        private final CardView cardView;


        private TaskDataViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitleTextView);
            checkBox = itemView.findViewById(R.id.checkBoxTask);
            imageView = itemView.findViewById(R.id.iconTaskCategory);
            cardView = itemView.findViewById(R.id.colorMarkCardView);
            date = itemView.findViewById(R.id.taskDateTextView);
            detailImageView = itemView.findViewById(R.id.detailImageView);
        }

        public void setEveryThing(TaskData taskData) {
            setColor(taskData);
            setTextTitle(taskData);
            setIconCategory(taskData);
        }

        /**
         * Sets color of left card view
         *
         * @param taskData    current task
         */
        public void setColor(TaskData taskData) {
            // urgent & important
            if (taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.Important) {
                cardView.setCardBackgroundColor(Colors.RED.getColor());
                detailImageView.setImageTintList(ColorStateList.valueOf((Colors.RED.getColor())));
                imageView.setImageTintList(ColorStateList.valueOf((Colors.RED.getColor())));
            }
            // urgent & not important
            else if (taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.NotImportant) {
                cardView.setCardBackgroundColor(Colors.BLUE.getColor());
                detailImageView.setImageTintList(ColorStateList.valueOf((Colors.BLUE.getColor())));
                imageView.setImageTintList(ColorStateList.valueOf((Colors.BLUE.getColor())));
            }
            // not urgent & important
            else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.Important) {
                cardView.setCardBackgroundColor(Colors.YELLOW.getColor());
                detailImageView.setImageTintList(ColorStateList.valueOf((Colors.YELLOW.getColor())));
                imageView.setImageTintList(ColorStateList.valueOf((Colors.YELLOW.getColor())));
            }
            // not urgent & not important
            else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.NotImportant) {
                cardView.setCardBackgroundColor(Colors.PINK.getColor());
                detailImageView.setImageTintList(ColorStateList.valueOf((Colors.PINK.getColor())));
                imageView.setImageTintList(ColorStateList.valueOf((Colors.PINK.getColor())));
            }
        }

        /**
         * Sets title of tasks
         */
        private void setTextTitle(TaskData task) {
            String titleText = task.getTaskTitleText();
            String dateText = setDateTextView(task);
            title.setText(titleText);
            date.setText(dateText);
        }

        /**
         * Merges date strings
         */
        // delete "/n"
        private String setDateTextView(TaskData task) {
            String dateView = "";
            if (task.getEndingDate() != null) {
                if (task.getStartingDate() != null)
                    dateView = task.getStartingDate() + " - ";
                dateView += task.getEndingDate();
            }
            return dateView;
        }

        /**
         * Sets icon of category
         */
        private void setIconCategory(TaskData task) {
            if (task.getCategory() != null) {
                switch (task.getCategory()) {
                    case Work -> imageView.setImageDrawable(ResourcesCompat.getDrawable(
                            context.getResources(), R.drawable.home_2, null));
                    case Private -> imageView.setImageDrawable(ResourcesCompat.getDrawable(
                            context.getResources(), R.drawable.briefcase_2, null));
                }
            }
        }
    }
}
