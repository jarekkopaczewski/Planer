package skills.future.planer.ui.day.views.matrix;

import android.widget.ProgressBar;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;

import skills.future.planer.db.task.TaskDataViewModel;

public class MatrixModelView extends ViewModel {


    private static ArrayList<TaskDataViewModel> taskDataViewModels;
    private static LifecycleOwner viewLifecycleOwner;
    private static ArrayList<MatrixListTotalAdapter> matrixAdapters;

    public void setUpModels(CalendarDay date) {
        var calendarDate = Calendar.getInstance();
        calendarDate.set(date.getYear(), date.getMonth(), date.getDay());
        long dateInLong = calendarDate.getTimeInMillis();
        for (int i = 0; i < 4; i++) {
            try {
                int finalI = i;
                taskDataViewModels.get(i)
                        .getCategorizedTaskDataFromDay(i, dateInLong)
                        .observe(viewLifecycleOwner, taskData -> {
                            matrixAdapters.get(finalI).setFilteredTaskList(taskData);
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setTaskDataViewModels(ArrayList<TaskDataViewModel> taskDataViewModels) {
        MatrixModelView.taskDataViewModels = taskDataViewModels;
    }

    public static void setViewLifecycleOwner(LifecycleOwner viewLifecycleOwner) {
        MatrixModelView.viewLifecycleOwner = viewLifecycleOwner;
    }

    public static void setMatrixAdapters(ArrayList<MatrixListTotalAdapter> matrixAdapters) {
        MatrixModelView.matrixAdapters = matrixAdapters;
    }
}
