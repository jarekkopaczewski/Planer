package skills.future.planer.db.task;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * View Model to keep a reference to the taskDataRepository and
 * an up-to-date list of all taskData.
 */
@Getter
public class TaskDataViewModel extends AndroidViewModel {
    /**
     * Reference to taskDataRepository
     */
    @Getter(AccessLevel.NONE)
    private final TaskDataRepository mRepository;
    /**
     * Up-to-date list of all taskData
     */
    private final LiveData<List<TaskData>> allTaskData, mImportantUrgentTaskData,
            mImportantNotUrgentTaskData, mNotImportantUrgentTaskData, mNotImportantNotUrgentTaskData;

    /**
     * Constructor require by viewModelProvider
     *
     * @param application reference to application
     */
    public TaskDataViewModel(Application application) {
        super(application);
        mRepository = new TaskDataRepository(application);
        allTaskData = mRepository.getListLiveData();
        mImportantUrgentTaskData = mRepository.getImportantUrgentTask();
        mImportantNotUrgentTaskData = mRepository.getImportantNotUrgent();
        mNotImportantUrgentTaskData = mRepository.getNotImportantUrgentTask();
        mNotImportantNotUrgentTaskData = mRepository.getNotImportantNotUrgent();
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
