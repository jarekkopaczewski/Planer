package skills.future.planer.ui.day.views.daylist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;

import java.util.List;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class TaskDayAdapter extends TaskTotalAdapter {

    public TaskDayAdapter(Context context, ComponentActivity activity) {
        super(context, activity, null);
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @NonNull
    @Override
    protected View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        return super.createViewOfItem(parent, layoutType);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDataViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void createListenerToExtendView(@NonNull TaskDataViewHolder holder) {
        super.createListenerToExtendView(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setDisplayedList(List<TaskData> words) {
        super.setDisplayedList(words);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
