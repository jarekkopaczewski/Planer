package skills.future.planer.db.task;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;


/**
 * Class implement separation of concerns
 */
public class TaskDataRepository {
    /**
     * Reference to taskDataDao
     */
    @Getter(AccessLevel.NONE)
    private final TaskDataDao taskDataDao;
    /**
     * List od all taskData
     */
    private LiveData<List<TaskData>> listLiveData;

    /**
     * Constructor of TaskDataRepository
     *
     * @param application require to get AppDatabase reference
     */
    public TaskDataRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDataDao = db.taskDataTabDao();
    }

    /**
     * Method insert taskData into database
     *
     * @param taskData which will be inserted
     */
    void insert(TaskData taskData) {
        taskData.setTaskDataId(taskDataDao.insert(taskData));
    }

    /**
     * Method edit taskData in database
     *
     * @param taskData which will be edited
     */
    public void edit(TaskData taskData) {
        taskDataDao.editOne(taskData);
    }

    /**
     * @return reference to list of all taskData
     */
    LiveData<List<TaskData>> getAllTaskData() {
        if (listLiveData == null)
            listLiveData = taskDataDao.getTaskData();
        return listLiveData;
    }

    /**
     * @return references to the task list based on quarter and day
     */
    LiveData<List<TaskData>> getCategorizedListLiveDataFromDay(int quarter, long date) throws Exception {
        return switch (quarter) {
            case 0 -> taskDataDao.getTaskData(Priorities.Important, TimePriority.Urgent, date);
            case 1 -> taskDataDao.getTaskData(Priorities.Important, TimePriority.NotUrgent, date);
            case 2 -> taskDataDao.getTaskData(Priorities.NotImportant, TimePriority.Urgent, date);
            case 3 -> taskDataDao.getTaskData(Priorities.NotImportant, TimePriority.NotUrgent, date);
            default -> throw new Exception("No such quarter: " + quarter);
        };
    }

    /**
     * @return references to the task list based on day
     */
    LiveData<List<TaskData>> getAllTaskDataFromDay(long date) {
        return taskDataDao.getTaskDataByDate(date);
    }

    /**
     * @return number of tasks from a particular day
     */
    int getNumberOfTaskByDate(long date) {
        return taskDataDao.getNumberOfTaskByDate(date);
    }

    /**
     * Method delete taskData from database
     *
     * @param taskData which will be inserted
     */
    void deleteTaskData(TaskData taskData) {
        taskDataDao.deleteOne(taskData);
        //new deleteTaskDataAsyncTask(taskDataDao).execute(taskData);
    }
/*
    /**
     * Class run asyncTask to insert taskData
     *//*
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

    //**
     * Class run asyncTask to delete taskData from database
     *//*
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
    }*/
}
