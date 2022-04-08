package skills.future.planer.ui.tasklist.viewholders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.tasklist.Colors;

@Getter
public class TaskDataViewHolder extends RecyclerView.ViewHolder {
    private final TextView title, date;
    private final CheckBox checkBox;
    private final ImageView iconTaskCategory, detailImageView;
    private final CardView cardView;
    private Context context;


    public TaskDataViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        title = itemView.findViewById(R.id.taskTitleTextView);
        checkBox = itemView.findViewById(R.id.checkBoxTask);
        iconTaskCategory = itemView.findViewById(R.id.iconTaskCategory);
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
     * Sets color of items in taskView
     *
     * @param taskData current task
     */
    protected void setColor(TaskData taskData) {
        // urgent & important
        if (taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.Important) {
            cardView.setCardBackgroundColor(Colors.RED.getColor());
            detailImageView.setImageTintList(ColorStateList.valueOf((Colors.RED.getColor())));
            iconTaskCategory.setImageTintList(ColorStateList.valueOf((Colors.RED.getColor())));
        }
        // urgent & not important
        else if (taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.NotImportant) {
            cardView.setCardBackgroundColor(Colors.BLUE.getColor());
            detailImageView.setImageTintList(ColorStateList.valueOf((Colors.BLUE.getColor())));
            iconTaskCategory.setImageTintList(ColorStateList.valueOf((Colors.BLUE.getColor())));
        }
        // not urgent & important
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.Important) {
            cardView.setCardBackgroundColor(Colors.YELLOW.getColor());
            detailImageView.setImageTintList(ColorStateList.valueOf((Colors.YELLOW.getColor())));
            iconTaskCategory.setImageTintList(ColorStateList.valueOf((Colors.YELLOW.getColor())));
        }
        // not urgent & not important
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.NotImportant) {
            cardView.setCardBackgroundColor(Colors.PINK.getColor());
            detailImageView.setImageTintList(ColorStateList.valueOf((Colors.PINK.getColor())));
            iconTaskCategory.setImageTintList(ColorStateList.valueOf((Colors.PINK.getColor())));
        }
    }

    /**
     * Sets title of tasks
     */
    private void setTextTitle(TaskData task) {
        title.setText(task.getTaskTitleText());
        date.setText(setDateTextView(task));
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
                case Work -> iconTaskCategory.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.home_2, null));
                case Private -> iconTaskCategory.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.briefcase_2, null));
            }
        }
    }


}
