package skills.future.planer.ui.day.views.daylist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.day.DayFragmentDirections;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;

public class TaskDayAdapter extends TaskTotalAdapter {

    public TaskDayAdapter(Context context, TaskDataViewModel mTaskViewModel) {
        super(context, mTaskViewModel);
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
    public TaskData getTaskDataAtPosition(int position) {
        return super.getTaskDataAtPosition(position);
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
    protected void createListenerToEditButton(@NonNull TaskDataViewHolder holder, int position) {
        if (holder.itemView.findViewById(R.id.detailImageView) != null)
            holder.itemView.findViewById(R.id.detailImageView).setOnClickListener(e ->
                    Navigation.findNavController(holder.itemView)
                            .navigate(DayFragmentDirections.actionNavDayToTaskListCreatorFragment(getFullTaskList().get(position).getTaskDataId())));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setFilteredTaskList(List<TaskData> words) {
        super.setFilteredTaskList(words);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
