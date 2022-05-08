package skills.future.planer.ui.day.views.matrix;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.tools.DatesParser;

public class MatrixModelView extends ViewModel {


    private static ArrayList<TaskDataViewModel> taskDataViewModels;
    private static LifecycleOwner viewLifecycleOwner;
    private static ArrayList<MatrixListTotalAdapter> matrixAdapters;

    public void setUpModels(CalendarDay date) {
        long dateInLong = DatesParser.toMilliseconds(date);
        for (int i = 0; i < 4; i++) {
            try {
                int finalI = i;
                taskDataViewModels.get(i)
                        .getCategorizedTaskDataFromDay(i, dateInLong)
                        .observe(viewLifecycleOwner, taskData ->
                                matrixAdapters.get(finalI).setFilteredTaskList(taskData));
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
