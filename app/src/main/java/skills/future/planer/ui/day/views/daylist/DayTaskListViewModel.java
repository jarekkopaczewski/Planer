package skills.future.planer.ui.day.views.daylist;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import lombok.Getter;
import lombok.Setter;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;

public class DayTaskListViewModel extends ViewModel {

    private static TaskDataViewModel mWordViewModel;
    private static TaskTotalAdapter taskDayAdapter;
    private static LifecycleOwner viewLifecycleOwner;

    /**
     * Sets observer for list for concrete day
     */
    public void updateDate(CalendarDay date) {
        mWordViewModel.getAllTaskDataFromDay(date)
                .observe(viewLifecycleOwner,
                        taskData -> taskDayAdapter.setFilteredTaskList(taskData));
    }

    public void setWordViewModel(TaskDataViewModel mWordViewModel) {
        DayTaskListViewModel.mWordViewModel = mWordViewModel;
    }

    public void setTaskDayAdapter(TaskTotalAdapter taskDayAdapter) {
        DayTaskListViewModel.taskDayAdapter = taskDayAdapter;
    }

    public void setLifecycleOwner(LifecycleOwner viewLifecycleOwner) {
        DayTaskListViewModel.viewLifecycleOwner = viewLifecycleOwner;
    }

    public static TaskDataViewModel getMWordViewModel() {
        return mWordViewModel;
    }

    public static TaskTotalAdapter getTaskDayAdapter() {
        return taskDayAdapter;
    }

    public static LifecycleOwner getViewLifecycleOwner() {
        return viewLifecycleOwner;
    }
}
