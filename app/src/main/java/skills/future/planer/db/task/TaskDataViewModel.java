package skills.future.planer.db.task;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.List;

import skills.future.planer.tools.DatesParser;

/**
 * View Model to keep a reference to the taskDataRepository and
 * an up-to-date list of all taskData.
 */
public class TaskDataViewModel extends AndroidViewModel {
    /**
     * Reference to taskDataRepository
     */

    private final TaskDataRepository mRepository;
    /**
     * Up-to-date list of all taskData
     */
    private final LiveData<List<TaskData>> allTaskData;
    private LiveData<List<TaskData>> categorizedTaskDataFromDay;
    private final Calendar calendarDate;

    /**
     * Constructor require by viewModelProvider
     *
     * @param application reference to application
     */
    public TaskDataViewModel(Application application) {
        super(application);
        mRepository = new TaskDataRepository(application);
        allTaskData = mRepository.getAllTaskData();
        calendarDate = Calendar.getInstance();
    }

    public LiveData<List<TaskData>> getAllTaskData() {
        return allTaskData;
    }

    /**
     * Delegates getting tasks to TaskDataRepository
     *
     * @param quarter quarter number
     * @param date    date for which we want to get data
     */
    public LiveData<List<TaskData>> getCategorizedTaskDataFromDay(int quarter, long date)
            throws Exception {
        return mRepository.getCategorizedListLiveDataFromDay(quarter, date);
    }

    public LiveData<List<TaskData>> getAllTaskDataFromDay(CalendarDay date) {
        return mRepository.getAllTaskDataFromDay(DatesParser.toMilliseconds(date));
    }

    /**
     * Delegates getting number of tasks to TaskDataRepository
     *
     * @param date date for which we want to get data
     */
    public int getNumberOfTaskByDate(CalendarDay date) {
        return mRepository.getNumberOfTaskByDate(DatesParser.toMilliseconds(date));
    }

    /**
     * Delegates insertion to TaskDataRepository
     *
     * @param taskData which will be inserted
     */
    public void insert(TaskData taskData) {
        mRepository.insert(taskData);
    }

    /**
     * Method edit taskData in database
     *
     * @param taskData which will be edited
     */
    public void edit(TaskData taskData) {
        mRepository.edit(taskData);
    }

    /**
     * Delegates deletion to TaskDataRepository
     *
     * @param taskData which will be deleted
     */
    public void deleteTaskData(TaskData taskData) {
        mRepository.deleteTaskData(taskData);
    }

    public TaskData findById(Long taskId) throws Exception {
        return mRepository.findById(taskId);
    }
}
