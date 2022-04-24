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

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.tasklist.Colors;

public class TaskDataViewHolderExtended extends TaskDataViewHolder {


    private final Context context;
    private final ImageView iconPriorities;
    private final ImageView iconTimePriority;
    private final TextView taskDescriptionView;

    public TaskDataViewHolderExtended(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        taskDescriptionView = itemView.findViewById(R.id.taskDescriptionView);
        iconPriorities = itemView.findViewById(R.id.iconPriorities);
        iconTimePriority = itemView.findViewById(R.id.iconTimePriority);
    }


    @Override
    public void setEveryThing(TaskData taskData) {
        super.setEveryThing(taskData);
        setIconPriority(taskData);
        setIconTimePriority(taskData);
        setTaskDescriptionText(taskData);
        setColor(taskData);
    }

    /**
     * Sets description of task
     *
     * @param task current task
     */
    private void setTaskDescriptionText(@NonNull TaskData task) {
        taskDescriptionView.setText(task.getTaskDetailsText());
    }

    /**
     * Sets color of items in taskView
     *
     * @param taskData current task
     */
    @Override
    protected void setColor(TaskData taskData) {
        super.setColor(taskData);
        ColorStateList color = ColorStateList.valueOf(Colors.getColorFromPreferences("urgentImportant", getContext()));

        if (taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.NotImportant)
            color = ColorStateList.valueOf(Colors.getColorFromPreferences("urgentNotImportant", getContext()));
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.Important)
            color = ColorStateList.valueOf(Colors.getColorFromPreferences("notUrgentImportant", getContext()));
        else if (taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.NotImportant)
            color = ColorStateList.valueOf(Colors.getColorFromPreferences("notUrgentNotImportant", getContext()));

        iconTimePriority.setImageTintList(color);
        iconPriorities.setImageTintList(color);
    }

    /**
     * Sets icon of priority
     */
    private void setIconPriority(TaskData task) {
        if (task.getPriorities() != null) {
            switch (task.getPriorities()) {
                case Important -> iconPriorities.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.star, null));
                case NotImportant -> iconPriorities.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.trash, null));
            }
        }
    }

    /**
     * Sets icon of time priority
     */
    private void setIconTimePriority(TaskData task) {
        if (task.getTimePriority() != null) {
            switch (task.getTimePriority()) {
                case Urgent -> iconTimePriority.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.fire, null));
                case NotUrgent -> iconTimePriority.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.snail, null));
            }
        }
    }

    @Override
    public CardView getCardView() {
        return super.getCardView();
    }

    @Override
    public CheckBox getCheckBox() {
        return super.getCheckBox();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public TextView getDate() {
        return super.getDate();
    }

    @Override
    public ImageView getDetailImageView() {
        return super.getDetailImageView();
    }

    @Override
    public ImageView getIconTaskCategory() {
        return super.getIconTaskCategory();
    }

    @Override
    public TextView getTitle() {
        return super.getTitle();
    }
}
