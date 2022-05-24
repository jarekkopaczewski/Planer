package skills.future.planer.ui.tasklist.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;

import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;
import skills.future.planer.ui.tasklist.AddTaskActivity;
import skills.future.planer.ui.tasklist.Colors;

public class TaskDataViewHolderExtended extends TaskDataViewHolder {


    private final Context context;
    private final ImageView iconPriorities;
    private final ImageView iconTimePriority;
    private final ImageView trashImageView;
    private final ImageView detailImageView;
    private final TextView taskDescriptionView;
    private final Chip goalChip;
    private final ComponentActivity activity;

    public TaskDataViewHolderExtended(View itemView, Context context, ComponentActivity activity) {
        super(itemView, context, activity);
        this.context = context;
        taskDescriptionView = itemView.findViewById(R.id.taskDescriptionView);
        iconPriorities = itemView.findViewById(R.id.iconPriorities);
        iconTimePriority = itemView.findViewById(R.id.iconTimePriority);
        goalChip = itemView.findViewById(R.id.goalChip);
        detailImageView = itemView.findViewById(R.id.detailImageView);
        trashImageView = itemView.findViewById(R.id.trashImageView);
        this.activity = activity;
    }


    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        if (element instanceof TaskData taskData) {
            super.setEveryThing(taskData);
            setIconPriority(taskData);
            setIconTimePriority(taskData);
            setTaskDescriptionText(taskData);
            setColor(taskData);
            setGoalChip(taskData);
            // listeners
            createListenerToEditButton(taskData);
            createListenerToTrashButton(taskData);
        }
    }

    /**
     * Creates listener to edit button which starts a AddTaskActivity
     */
    private void createListenerToEditButton(TaskData taskData) {
        detailImageView.setOnClickListener(e -> {
            var intent = new Intent(activity, AddTaskActivity.class);
            var bundle = new Bundle();
            bundle.putLong("taskToEditId", taskData.getTaskDataId());
            intent.putExtras(bundle);
            activity.startActivity(intent);
        });
    }

    /**
     * Creates listener to edit button which delete task
     */
    private void createListenerToTrashButton(TaskData taskData) {
        trashImageView.setOnClickListener(e -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

            builder.setTitle(R.string.confirm_deletion);
            builder.setMessage(R.string.confirm_deletion_2);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.removetask);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) { new ViewModelProvider(activity).get(TaskDataViewModel.class).deleteTaskData(taskData); }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                this.itemView.startAnimation(animation);
                dialog.dismiss();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        });
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
    protected void setColor(@NonNull TaskData taskData) {
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
    private void setIconPriority(@NonNull TaskData task) {
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
    private void setIconTimePriority(@NonNull TaskData task) {
        if (task.getTimePriority() != null) {
            switch (task.getTimePriority()) {
                case Urgent -> iconTimePriority.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.fire, null));
                case NotUrgent -> iconTimePriority.setImageDrawable(ResourcesCompat.getDrawable(
                        context.getResources(), R.drawable.snail, null));
            }
        }
    }

    private void setGoalChip(@NonNull TaskData task) {
        GoalsViewModel goalsViewModel = new ViewModelProvider(activity).get(GoalsViewModel.class);
        GoalData goal = goalsViewModel.findById(task.getForeignKeyToGoal());
        if (goal != null) {
            goalChip.setVisibility(View.VISIBLE);
            String goalText = goal.getTitle();
            if (goalText.length() > 20) {
                goalText = goalText.substring(0, 17) + "...";
            }
            goalChip.setText(goalText);
        } else {
            goalChip.setVisibility(View.INVISIBLE);
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
