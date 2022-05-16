package skills.future.planer.ui.tasklist.viewholders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.goals.pager.recycler.ICustomViewHolder;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;
import skills.future.planer.ui.tasklist.Colors;

@Getter
public class TaskDataViewHolder extends ICustomViewHolder {
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

    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        if (element instanceof TaskData taskData) {
            setColor(taskData);
            setTextTitle(taskData);
            setIconCategory(taskData);
            setCheckBoxListener(taskData);
        }
    }

    /**
     * Sets listener for done/not done checkBox
     *
     * @param taskData which will be updated
     */
    private void setCheckBoxListener(TaskData taskData) {
        checkBox.setChecked(taskData.getStatus());

        checkBox.setOnClickListener(e -> {
            taskData.setStatus(checkBox.isChecked());
            //todo zamioenić ma TaskDataModelView
            var taskDataDao = AppDatabase.getInstance(this.getContext()).taskDataTabDao();

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scalezoom3);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    taskDataDao.editOne(taskData);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            itemView.startAnimation(animation);
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

        String dateView = "";
        if (task.getEndingDate() != 0) {
            if (task.getStartingDate() != 0) {
                dateView = DatesParser.toSting(task.getStartingCalendarDate()) + " - ";
            }
            dateView += DatesParser.toSting(task.getEndingCalendarDate());
        }
        return dateView;
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
