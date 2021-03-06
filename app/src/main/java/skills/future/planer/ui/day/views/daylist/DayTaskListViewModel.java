package skills.future.planer.ui.day.views.daylist;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;

public class DayTaskListViewModel extends ViewModel {

    private static TaskDataViewModel taskDataViewModel;
    private static TaskDayAdapter taskDayAdapter;
    private static LifecycleOwner viewLifecycleOwner;

    public static TaskDataViewModel getMWordViewModel() {
        return taskDataViewModel;
    }

    /**
     * Sets observer for list for concrete day
     */
    public void updateDate(CalendarDay date) {
        taskDataViewModel.getAllTaskDataFromDay(date)
                .observe(viewLifecycleOwner,
                        taskData -> taskDayAdapter.setDisplayedList(taskData));
    }

    public void setTaskDayAdapter(TaskDayAdapter taskDayAdapter) {
        DayTaskListViewModel.taskDayAdapter = taskDayAdapter;
    }

    public void setLifecycleOwner(LifecycleOwner viewLifecycleOwner) {
        DayTaskListViewModel.viewLifecycleOwner = viewLifecycleOwner;
    }

    public void setTaskViewModel(TaskDataViewModel mWordViewModel) {
        DayTaskListViewModel.taskDataViewModel = mWordViewModel;
    }

    public static TaskDayAdapter getTaskDayAdapter() {
        return taskDayAdapter;
    }

    public static LifecycleOwner getViewLifecycleOwner() {
        return viewLifecycleOwner;
    }
}
