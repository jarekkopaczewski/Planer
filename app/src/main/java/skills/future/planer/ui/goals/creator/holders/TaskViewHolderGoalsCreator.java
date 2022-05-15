package skills.future.planer.ui.goals.creator.holders;

import android.content.Context;
import android.view.View;

import androidx.activity.ComponentActivity;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class TaskViewHolderGoalsCreator extends TaskDataViewHolder {


    public TaskViewHolderGoalsCreator(View itemView, Context context, ComponentActivity activity) {
        super(itemView, context, activity);
    }

    @Override
    protected void setCheckBoxListener(TaskData taskData) {
    }
}
