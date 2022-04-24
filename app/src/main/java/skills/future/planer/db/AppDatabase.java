package skills.future.planer.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataDao;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

@Database(entities = {TaskData.class}, exportSchema = false, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    private static final String DB_NAME = "planer2.db";
    private static AppDatabase sInstance;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    public abstract TaskDataDao taskDataTabDao();

    public static AppDatabase getInstance(final Context context/*, final AppExecutors executors*/) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    Log.d(LOG_TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                public void onOpen(SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(sInstance).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TaskDataDao mDao;
        //String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(AppDatabase db) {
            mDao = db.taskDataTabDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();
            CalendarDay day = CalendarDay.today();
            CalendarDay day2 = CalendarDay.from(2022, 4, 21);
            CalendarDay day3 = CalendarDay.from(2022, 4, 23);
            CalendarDay day4 = CalendarDay.from(2022, 4, 7);
            CalendarDay day5 = CalendarDay.from(2022, 4, 9);
            CalendarDay day6 = CalendarDay.from(2022, 4, 26);

            for (int i = 0; i <= 4; i++) {
                TaskData word = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent, "1iu", "", day2, day3);
                mDao.insert(word);
            }
            for (int i = 0; i <= 4; i++) {
                TaskData word = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent, "1iu", "", day3, day6);
                mDao.insert(word);
            }
            for (int i = 0; i <= 4; i++) {
                TaskData word = new TaskData(TaskCategory.Work, Priorities.NotImportant, TimePriority.Urgent, "2niu", "", day2, day3);
                mDao.insert(word);
            }
            for (int i = 0; i <= 4; i++) {
                TaskData word = new TaskData(TaskCategory.Private, Priorities.Important, TimePriority.NotUrgent, "3inu", "", day4, day2);
                mDao.insert(word);
            }
            for (int i = 0; i <= 4; i++) {
                TaskData word = new TaskData(TaskCategory.Private, Priorities.NotImportant, TimePriority.NotUrgent, "4ninu", "", day4, day5);
                mDao.insert(word);
            }
            for (int i = 0; i < 10; i++) {
                TaskData word = new TaskData(TaskCategory.Work,
                        Priorities.Important,
                        TimePriority.Urgent,
                        "Napisać aplikacje opierającą się na Remote Method Invocation",
                        "Zaimplementuj rozproszony system imitujący działanie sieci " +
                                "tablic reklamowych, na których cyklicznie wyświetlane są zadane teksty.\n" +
                                "Wymiana danych pomiędzy elementami systemu powinna odbywać się " +
                                "poprzez gniazda SSL, z wykorzystaniem menadżera bezpieczeństwa i plików polityki.", day, day2);
                mDao.insert(word);
            }

            /*for (int i = 0; i <= 1; i++) {
                TaskData word = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent, "1","",day,day);
                mDao.insert(word);
            }
            for (int i = 0; i <= 4; i++) {
                TaskData word = new TaskData(TaskCategory.Work, Priorities.NotImportant, TimePriority.Urgent, "2niu","",day2,day2);
                mDao.insert(word);
            }
            for (int i = 0; i <=4; i++) {
                TaskData word = new TaskData(TaskCategory.Private, Priorities.Important, TimePriority.NotUrgent, "3inu","",day3,day3);
                mDao.insert(word);
            }
            for (int i = 0; i <= 4; i++) {
                TaskData word = new TaskData(TaskCategory.Private, Priorities.NotImportant, TimePriority.NotUrgent, "4ninu","",day4,day4);
                mDao.insert(word);
            }*/
            return null;
        }
    }
}
