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

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

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
    private final Context context;
    private final View item;


    public TaskDataViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        this.item = itemView;
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
        setCheckBoxListener(taskData);
    }

    /**
     * Sets listener for done/not done checkBox
     *
     * @param taskData
     */
    private void setCheckBoxListener(TaskData taskData) {
        checkBox.setChecked(taskData.getStatus());

        checkBox.setOnClickListener(e -> {
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
    protected void setColor(@NonNull TaskData taskData) {
        int color = Colors.getColorFromPreferences("urgentImportant", getContext());

        if (taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.NotImportant)
            color = Colors.getColorFromPreferences("urgentNotImportant", getContext());
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.Important)
            color = Colors.getColorFromPreferences("notUrgentImportant", getContext());
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.NotImportant)
            color = Colors.getColorFromPreferences("notUrgentNotImportant", getContext());

        cardView.setCardBackgroundColor(color);
//        detailImageView.setImageTintList(ColorStateList.valueOf(color));
        iconTaskCategory.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets title of tasks
     */
    private void setTextTitle(@NonNull TaskData task) {
        title.setText(task.getTaskTitleText());
        date.setText(setDateTextView(task));
    }

    /**
     * Convert dates in long to string
     */
    private String setDateTextView(@NonNull TaskData task) {
        Calendar calendar = Calendar.getInstance();

        String dateView = "";
        if (task.getEndingDate() != 0) {
            if (task.getStartingDate() != 0) {
                calendar.setTime(Date.from(Instant.ofEpochMilli(task.getStartingDate())));
                dateView = generateDayInString(calendar) + " - ";
            }
            calendar.setTime(Date.from(Instant.ofEpochMilli(task.getEndingDate())));
            dateView += generateDayInString(calendar);
        }
        return dateView;
    }

    /**
     * Generate date string from calendar format
     */
    @NonNull
    private String generateDayInString(@NonNull Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH)
                + "."
                + calendar.get(Calendar.MONTH)
                + "."
                + calendar.get(Calendar.YEAR);
    }

    /**
     * Sets icon of category
     */
    private void setIconCategory(@NonNull TaskData task) {
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
