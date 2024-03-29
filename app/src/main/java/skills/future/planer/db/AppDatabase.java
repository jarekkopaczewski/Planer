package skills.future.planer.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.util.Calendar;

import lombok.SneakyThrows;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsDao;
import skills.future.planer.db.habit.HabitDao;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitDuration;
import skills.future.planer.db.summary.SummaryDao;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataDao;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.tools.DatesParser;

@Database(entities = {TaskData.class, HabitData.class, GoalData.class, SummaryData.class},
        exportSchema = false, version = 10)
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    private static final String DB_NAME = "planer4.db";
    private static AppDatabase sInstance;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    public abstract TaskDataDao taskDataTabDao();

    public abstract HabitDao habitDao();

    public abstract GoalsDao goalsDao();

    public abstract SummaryDao summaryDao();

    public static AppDatabase getInstance(final Context context/*, final AppExecutors executors*/) {


        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()/*.addCallback(sRoomDatabaseCallback)*/.build();
        }

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
        private final SummaryDao summaryDao;
        //String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(AppDatabase db) {
            super();
            mDao = db.taskDataTabDao();
            habitDao = db.habitDao();
            goalsDao = db.goalsDao();
            summaryDao = db.summaryDao();
        }

        @SneakyThrows
        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();
            habitDao.deleteAll();
            //goalsDao.deleteAll();
            summaryDao.deleteAll();
            CalendarDay day = CalendarDay.today();
            CalendarDay day3 = CalendarDay.from(2022, 6, 23);
            CalendarDay day2 = CalendarDay.from(2022, 6, 5);
            CalendarDay day4 = CalendarDay.from(2022, 6, 15);
            CalendarDay day5 = CalendarDay.from(2022, 6, 30);
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
            habitDao.insert(new HabitData("Nauczyć się francuskiego", "1111111",
                    HabitDuration.Short, DatesParser.toLocalDate(day2), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) + 1));
            habitDao.insert(new HabitData("Programować", "1111111",
                    HabitDuration.Short, DatesParser.toLocalDate(day2), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) + 2));

//
//
//            try {
//                var goal = new GoalData("Cel " + 1, "Material is the metaphor. " +
//                        " A material metaphor is the unifying theory of a rationalized space and a system of motion." +
//                        " The material is grounded in tactile reality, inspired by the study of paper and ink, yet " +
//                        " technologically advanced and open to imagination and magic." +
//                        " Surfaces and edges of the material provide visual cues that are grounded in reality. The " +
//                        " use of familiar tactile attributes helps users quickly understand affordances. Yet the" +
//                        " flexibility of the material creates new affordances that supercede those in the physical " +
//                        " world, without breaking the rules of physics." +
//                        " The fundamentals of light, surface, and movement are key to conveying how objects move, " +
//                        " interact, and exist in space and in relation to each other. Realistic lighting shows " +
//                        " seams, divides space, and indicates moving parts." +
//                        " Bold, graphic, intentional.", LocalDate.of(2022, 1, 1));
//                goal.setGoalId(goalsDao.insert(goal));

//                TaskData word = new TaskData(TaskCategory.Private, Priorities.NotImportant, TimePriority.NotUrgent, "Zadanie nieważne i niepilne z celem" + counter, "", day2, day3, goal.getGoalId());
//                word.setTaskDataId(mDao.insert(word));
//
//                var goal2 =
//                        new GoalData("Cel " + "agagasg", "opisasgasgagasg", LocalDate.of(2022, 2, 1));
//                goal2.setGoalId(goalsDao.insert(goal2));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            String test = "Material is the metaphor.A material metaphor is the unifying theory of a rationalized space and a system of motion. The material is grounded in tactile reality, inspired by the study of paper and ink, yet technologically advanced and open to imagination and magic. Surfaces and edges of the material provide visual cues that are grounded in reality. The Material is the metaphor. A material metaphor is the unifying theory of a rationalized space and a system of motion. The material is grounded in tactile reality, inspired by the study of paper and ink, yet technologically advanced and open to imagination and magic. Surfaces and edges of the material provide visual cues that are grounded in reality. The Material is the metaphor. A material metaphor is the unifying theory of a rationalized space and a system of motion. The material is grounded in tactile reality, inspired by the study of paper and ink, yet technologically advanced and open to imagination and magic. Surfaces and edges of the material provide visual cues that are grounded in reality. The";
            String test2 = "Material is the metaphor.A material metaphor is the unifying theory of a rationalized space and a system of motion.";

            CalendarDay calendarDay = CalendarDay.from(2021, 12, 13);
            LocalDate localDate = DatesParser.toLocalDate(calendarDay);
            LocalDate localDate1 = DatesParser.toLocalDate(CalendarDay.today());


            for( int i = 0; i < 18; i++)
            {
                if(localDate1.isAfter(localDate.plusMonths(i)))
                    summaryDao.insert(new SummaryData(test2, test2, test2, localDate.plusMonths(i), SummaryType.monthSummary));
            }

            for( int i = 0; i < 44; i++)
            {
                if(localDate1.isAfter(localDate.plusDays(i*7)))
                    summaryDao.insert(new SummaryData(test, test, test, localDate.plusDays(i*7), SummaryType.weekSummary));
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
