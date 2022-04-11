package skills.future.planer.ui.day.views.matrix;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.tasklist.Colors;

public class MatrixListTotalAdapter extends RecyclerView.Adapter<MatrixListTotalAdapter.TaskDataViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<TaskData> fullTaskList = new ArrayList<>();

    public MatrixListTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.fragment_task_in_matrix_done, parent, false);
        return new TaskDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDataViewHolder holder, int position) {
        if (fullTaskList != null) {
            TaskData current = fullTaskList.get(position);
            holder.setEveryThing(current);
        } else {
            holder.title.setText("No Word");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredTaskList(List<TaskData> words) {
        fullTaskList = words;
        notifyDataSetChanged();
    }

    public long getItemId(int position) {
        return position;
    }

    public Integer getDone() {
        Integer count = 0;
        for (var item : fullTaskList)
            if (item.getStatus())
                count++;
        return count;
    }

    @Override
    public int getItemCount() {
        if (fullTaskList != null)
            return fullTaskList.size();
        else return 0;
    }

    class TaskDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView done;
        private final CardView taskCard;

        private TaskDataViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitleTextView);
            done = itemView.findViewById(R.id.doneImageView);
            taskCard = itemView.findViewById(R.id.taskCard);
        }

        public void setEveryThing(TaskData taskData) {
            setTextTitle(taskData);
            checkIsDone(taskData);
        }

        private void setTextTitle(TaskData task) {
            String titleText = task.getTaskTitleText();
            title.setText(titleText);
        }

        @SuppressLint("ResourceAsColor")
        public void checkIsDone(TaskData taskData) {
            if (taskData.getStatus()) {
                done.setVisibility(VISIBLE);
                taskCard.setCardBackgroundColor(context.getResources().getColor(R.color.background_2));
            } else {
                done.setVisibility(View.INVISIBLE);
                taskCard.setCardBackgroundColor(context.getResources().getColor(R.color.foreground));
            }
        }
    }
}
