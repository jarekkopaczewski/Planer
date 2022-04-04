package skills.future.planer.db.task;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskDataViewModel extends AndroidViewModel {
    private TaskDataRepository mRepository;

    private LiveData<List<TaskData>> mAllWords;

    public TaskDataViewModel (Application application) {
        super(application);
        mRepository = new TaskDataRepository(application);
        mAllWords = mRepository.getAllTaskData();
    }

    public LiveData<List<TaskData>> getAllWords() { return mAllWords; }

    public void insert(TaskData word) { mRepository.insert(word); }

    public void deleteTaskData(TaskData taskData){
        mRepository.deleteTaskData(taskData);
    }
}
