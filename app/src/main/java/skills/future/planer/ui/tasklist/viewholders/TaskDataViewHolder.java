package skills.future.planer.ui.tasklist.viewholders;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.AppDatabase;
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
        checkBox.setChecked(taskData.getStatus());

        checkBox.setOnClickListener(e->{
            taskData.setStatus(checkBox.isChecked());
            var taskDataDao = AppDatabase.getInstance(this.getContext()).taskDataTabDao();
            taskDataDao.editOne(taskData);
        });
    }

    /**
     * Sets color of items in taskView
     *
     * @param taskData current task
     */
    protected void setColor(TaskData taskData) {
        int color = Colors.getColorFromPreferences("urgentImportant", getContext());

        if (taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.NotImportant)
            color = Colors.getColorFromPreferences("urgentNotImportant", getContext());
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.Important)
            color = Colors.getColorFromPreferences("notUrgentImportant", getContext());
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.NotImportant)
            color = Colors.getColorFromPreferences("notUrgentNotImportant", getContext());

        cardView.setCardBackgroundColor(color);
        detailImageView.setImageTintList(ColorStateList.valueOf(color));
        iconTaskCategory.setImageTintList(ColorStateList.valueOf(color));
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
                case Private -> iconTaskCategory.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.home_2, null));
                case Work -> iconTaskCategory.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.briefcase_2, null));
            }
        }
    }


}
