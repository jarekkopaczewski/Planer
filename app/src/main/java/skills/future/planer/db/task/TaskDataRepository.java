package skills.future.planer.db.task;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;


/**
 * Class implement separation of concerns
 *
 * @author Mikołaj Szymczyk
 */
public class TaskDataRepository {
    /**
     * Reference to taskDataDao
     */
    private final TaskDataDao taskDataDao;
    /**
     * List od all taskData
     */
    private final LiveData<List<TaskData>> listLiveData;
    /**
     * Categorized taskData
     */
    protected LiveData<List<TaskData>> importantUrgentTask, importantNotUrgent,
            notImportantUrgentTask, notImportantNotUrgent;

    /**
     * Constructor of TaskDataRepository
     *
     * @param application require to get AppDatabase reference
     * @author Mikołaj Szymczyk
     */
    TaskDataRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDataDao = db.taskDataTabDao();
        listLiveData = taskDataDao.getTaskData();
        new SelectAsyncTask(taskDataDao, this).execute();
    }

    /**
     * Method start new asyncTask which insert taskData into database
     *
     * @param taskData which will be inserted
     * @author Mikołaj Szymczyk
     */
    void insert(TaskData taskData) {
        new InsertAsyncTask(taskDataDao).execute(taskData);
    }

    /**
     * @return reference to liveData list of all taskData
     * @author Mikołaj Szymczyk
     */
    LiveData<List<TaskData>> getAllTaskData() {
        return listLiveData;
    }

    /**
     * @return reference to liveData list of important and urgent task
     * @author Mikołaj Szymczyk
     */
    public LiveData<List<TaskData>> getImportantUrgentTask() {
        return importantUrgentTask;
    }

    /**
     * @return reference to liveData list of important and not urgent task
     * @author Mikołaj Szymczyk
     */
    public LiveData<List<TaskData>> getImportantNotUrgent() {
        return importantNotUrgent;
    }

    /**
     * @return reference to liveData list of not important and urgent task
     * @author Mikołaj Szymczyk
     */
    public LiveData<List<TaskData>> getNotImportantUrgentTask() {
        return notImportantUrgentTask;
    }

    /**
     * @return reference to liveData list of not important and not urgent task
     * @author Mikołaj Szymczyk
     */
    public LiveData<List<TaskData>> getNotImportantNotUrgent() {
        return notImportantNotUrgent;
    }

    /**
     * Method start new asyncTask which delete taskData from database
     *
     * @param taskData which will be inserted
     * @author Mikołaj Szymczyk
     */
    void deleteTaskData(TaskData taskData) {
        new deleteTaskDataAsyncTask(taskDataDao).execute(taskData);
    }

    /**
     * Class run asyncTask to insert taskData
     * @author Mikołaj Szymczyk
     */
    private static class InsertAsyncTask extends AsyncTask<TaskData, Void, Void> {
        private final TaskDataDao asyncTaskDao;

        InsertAsyncTask(TaskDataDao taskDataDao) {
            super();
            asyncTaskDao = taskDataDao;
        }

        @Override
        protected Void doInBackground(final TaskData... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Class run asyncTask to delete taskData from database
     * @author Mikołaj Szymczyk
     */
    private static class deleteTaskDataAsyncTask extends AsyncTask<TaskData, Void, Void> {
        private final TaskDataDao mAsyncTaskDao;

        deleteTaskDataAsyncTask(TaskDataDao dao) {
            super();
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TaskData... params) {
            mAsyncTaskDao.deleteOne(params[0]);
            return null;
        }
    }

    /**
     * Class run asyncTask which select categorized tasks
     * @author Mikołaj Szymczyk
     */
    private static class SelectAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TaskDataDao asyncTaskDao;
        private final TaskDataRepository categorisedTaskData;

        SelectAsyncTask(TaskDataDao taskDataDao, TaskDataRepository categorisedTaskData) {
            super();
            asyncTaskDao = taskDataDao;
            this.categorisedTaskData = categorisedTaskData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                categorisedTaskData.importantNotUrgent = asyncTaskDao.getTaskData(Priorities.Important, TimePriority.NotUrgent);
                categorisedTaskData.importantUrgentTask = asyncTaskDao.getTaskData(Priorities.Important, TimePriority.Urgent);
                categorisedTaskData.notImportantUrgentTask = asyncTaskDao.getTaskData(Priorities.NotImportant, TimePriority.Urgent);
                categorisedTaskData.notImportantNotUrgent = asyncTaskDao.getTaskData(Priorities.NotImportant, TimePriority.NotUrgent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
