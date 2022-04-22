package skills.future.planer.db.task;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.Getter;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.TimePriority;

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

    /**
     * Constructor require by viewModelProvider
     *
     * @param application reference to application
     */
    public TaskDataViewModel(Application application) {
        super(application);
        mRepository = new TaskDataRepository(application);
        allTaskData = mRepository.getAllTaskData();
    }

    public LiveData<List<TaskData>> getAllTaskData() {
        return allTaskData;
    }

    public LiveData<List<TaskData>> getCategorizedTaskDataFromDay(
            int quarter, long date) throws Exception {
        if(categorizedTaskDataFromDay == null)
            categorizedTaskDataFromDay = mRepository.getCategorizedListLiveDataFromDay(
                    quarter,date);
        return categorizedTaskDataFromDay;
    }
    public LiveData<List<TaskData>> getAllTaskDataFromDay(long date){
        return mRepository.getAllTaskDataFromDay(date);
    }

    /**
     * Method delegate insertion to TaskDataRepository
     *
     * @param taskData which will be inserted
     */
    public void insert(TaskData taskData) {
        mRepository.insert(taskData);
    }

    /**
     * Method delegate deletion to TaskDataRepository
     *
     * @param taskData which will be deleted
     */
    public void deleteTaskData(TaskData taskData) {
        mRepository.deleteTaskData(taskData);
    }
}
