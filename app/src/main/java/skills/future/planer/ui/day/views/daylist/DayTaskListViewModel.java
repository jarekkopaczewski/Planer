package skills.future.planer.ui.day.views.daylist;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;

public class DayTaskListViewModel extends ViewModel {

    private static TaskDataViewModel mWordViewModel;
    private static TaskTotalAdapter taskDayAdapter;
    private static LifecycleOwner viewLifecycleOwner;

    public void updateDate(CalendarDay date) {
        var calendarDate = Calendar.getInstance();
        calendarDate.set(date.getYear(), date.getMonth(), date.getDay());
        mWordViewModel.getAllTaskDataFromDay(calendarDate.getTimeInMillis())
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
