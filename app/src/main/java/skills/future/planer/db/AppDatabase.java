package skills.future.planer.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.database.TaskDataTabDao;

@Database(entities = {TaskData.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    private static final String DB_NAME = "planer2.db";
    private static AppDatabase sInstance;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    public abstract TaskDataTabDao taskDataTabDao();

    public static AppDatabase getInstance(final Context context/*, final AppExecutors executors*/) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    Log.d(LOG_TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries()
                            .build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
}
