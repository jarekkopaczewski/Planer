package skills.future.planer.db;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;

import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsDao;
import skills.future.planer.db.habit.HabitDao;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitDuration;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataDao;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.tools.DatesParser;

@Database(entities = {TaskData.class, HabitData.class, GoalData.class}, exportSchema = false, version = 7)
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    private static final String DB_NAME = "planer2.db";
    private static AppDatabase sInstance;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    public abstract TaskDataDao taskDataTabDao();

    public abstract HabitDao habitDao();

    public abstract GoalsDao goalsDao();

    public static AppDatabase getInstance(final Context context/*, final AppExecutors executors*/) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    Log.d(LOG_TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME).fallbackToDestructiveMigration()
                            .allowMainThreadQueries()/*.addCallback(sRoomDatabaseCallback)*/.build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(sInstance).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TaskDataDao mDao;
        private final HabitDao habitDao;
        private final GoalsDao goalsDao;
        //String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(AppDatabase db) {
            super();
            mDao = db.taskDataTabDao();
            habitDao = db.habitDao();
            goalsDao = db.goalsDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();
            habitDao.deleteAll();
            goalsDao.deleteAll();
            CalendarDay day = CalendarDay.today();
            CalendarDay day2 = CalendarDay.from(2022, 5, 5);
            CalendarDay day3 = CalendarDay.from(2022, 5, 23);
            CalendarDay day4 = CalendarDay.from(2022, 5, 15);
            CalendarDay day5 = CalendarDay.from(2022, 5, 30);
            var cal = Calendar.getInstance();
            int counter = 1;

            for (int i = 0; i < 3; i++) {
                TaskData word = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent, "Zadanie ważne i pilne" + counter, "", day2, day3);
                word.setTaskDataId(mDao.insert(word));
                counter++;
            }
            for (int i = 0; i < 3; i++) {
                TaskData word = new TaskData(TaskCategory.Work, Priorities.NotImportant, TimePriority.Urgent, "Zadanie nieważne i pilne" + counter, "", day2, day3);
                word.setTaskDataId(mDao.insert(word));
                counter++;
            }
            for (int i = 0; i <= 3; i++) {
                TaskData word = new TaskData(TaskCategory.Private, Priorities.Important, TimePriority.NotUrgent, "Zadanie ważne i niepilne" + counter, "", day4, day5);
                word.setTaskDataId(mDao.insert(word));
                counter++;
            }
            for (int i = 0; i <= 3; i++) {
                TaskData word = new TaskData(TaskCategory.Private, Priorities.NotImportant, TimePriority.NotUrgent, "Zadanie nieważne i niepilne" + counter, "", day4, day5);
                word.setTaskDataId(mDao.insert(word));
                counter++;
            }


            try {
                var goal = new GoalData("Cel " + 1, "opis", LocalDate.of(2022, 1, 1));
                goal.setGoalId(goalsDao.insert(goal));
                habitDao.insert(new HabitData("test", "1111111",
                        HabitDuration.Short, DatesParser.toLocalDate(day), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) + 2, goal.getGoalId()));
                habitDao.insert(new HabitData("test2", "1111111",
                        HabitDuration.Short, DatesParser.toLocalDate(day), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) + 4, goal.getGoalId()));
                TaskData word = new TaskData(TaskCategory.Private, Priorities.NotImportant, TimePriority.NotUrgent, "Zadanie nieważne i niepilne z celem" + counter, "", day2, day3, goal.getGoalId());
                word.setTaskDataId(mDao.insert(word));

                var goal2 =
                        new GoalData("Cel " + "agagasg", "opisasgasgagasg", LocalDate.of(2022, 2, 1));
                goal2.setGoalId(goalsDao.insert(goal2));
            } catch (Exception e) {
                e.printStackTrace();
            }

//            for (int i = 0; i <= 1; i++) {
//                TaskData word = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent, "Zadanie " + counter, "", day3, day6);
//                mDao.insert(word);
//                counter++;
//            }
//            for (int i = 0; i < 3; i++) {
//                TaskData word = new TaskData(TaskCategory.Work,
//                        Priorities.Important,
//                        TimePriority.Urgent,
//                        "Napisać aplikacje opierającą się na Remote Method Invocation",
//                        "Zaimplementuj rozproszony system imitujący działanie sieci " +
//                                "tablic reklamowych, na których cyklicznie wyświetlane są zadane teksty.\n" +
//                                "Wymiana danych pomiędzy elementami systemu powinna odbywać się " +
//                                "poprzez gniazda SSL, z wykorzystaniem menadżera bezpieczeństwa i plików polityki.", day, day2);
//                mDao.insert(word);
//                counter++;
//            }

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
