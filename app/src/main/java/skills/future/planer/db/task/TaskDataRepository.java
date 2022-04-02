package skills.future.planer.db.task;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import skills.future.planer.db.AppDatabase;


public class TaskDataRepository {
    private TaskDataDao taskDataDao;
    private LiveData<List<TaskData>> listLiveData;

    TaskDataRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        taskDataDao = db.taskDataTabDao();
        listLiveData = taskDataDao.getTaskData();
    }

    void insert(TaskData taskData){
        new InsertAsyncTask(taskDataDao).execute(taskData);
    }

    LiveData<List<TaskData>> getAllTaskData() {
        return listLiveData;
    }

    private static class InsertAsyncTask extends AsyncTask<TaskData,Void,Void> {
        private final TaskDataDao asyncTaskDao;
        InsertAsyncTask(TaskDataDao taskDataDao){
            asyncTaskDao = taskDataDao;
        }

        @Override
        protected Void doInBackground(final TaskData... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deleteTaskData(TaskData taskData)  {
        new deleteTaskDataAsyncTask(taskDataDao).execute(taskData);
    }

    private static class deleteTaskDataAsyncTask extends AsyncTask<TaskData, Void, Void> {
        private TaskDataDao mAsyncTaskDao;

        deleteTaskDataAsyncTask(TaskDataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TaskData... params) {
            mAsyncTaskDao.deleteOne(params[0]);
            return null;
        }
    }
}
