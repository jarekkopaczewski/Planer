package skills.future.planer.db.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.db.DBHandler;

public class TaskDataTable implements TaskDataTabInterface {
    public static final String TAB_TASK_DATA = "TASK_DATA_TABLE",
            COL_TASK_DATA_ID = "TASK_DATA_ID",
            COL_STATUS = "COL_STATUS",
            COL_CATEGORY = "COL_CATEGORY",
            COL_PRIORITIES = "COL_PRIORITIES",
            COL_TIME_PRIORITIES = " COL_TIME_PRIORITIES",
            COL_TASK_TITLE = "COL_TASK_TITLE",
            COL_TASK_DETAILS = "COL_TASK_DETAILS",
            COL_STARTING_DATE = "COL_STARTING_DATE",
            COL_ENDING_DATE = "COL_ENDING_DATE";
    DBHandler database;

    public TaskDataTable(Context context) {
        database = new DBHandler(context);
    }

    public static void createTaskDataTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TAB_TASK_DATA + " (" +
                COL_TASK_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_STATUS + " BOOL ," +
                COL_CATEGORY + "  INTEGER, " +
                COL_PRIORITIES + " INTEGER, " +
                COL_TIME_PRIORITIES + " INTEGER, " +
                COL_TASK_TITLE + " TEXT," +
                COL_TASK_DETAILS + " TEXT," +
                COL_STARTING_DATE + " TEXT, " +
                COL_ENDING_DATE + " TEXT)";
        db.execSQL(query);
    }

    /**
     * Method searching database for TaskData with given ID
     *
     * @param id od searched TaskData
     * @return TaskData if funded or null if not
     * @author Mikołaj Szymczyk
     */
    @Override
    public TaskData findById(int id) throws Exception {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "SELECT * FROM " + TAB_TASK_DATA + " WHERE " + COL_TASK_DATA_ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        TaskData taskData;
        if (cursor.moveToFirst()) {
            //todo przerzucić powtórzony kod do innej metody
            String startingDayRes = cursor.getString(7), endingDayRes = cursor.getString(8);
            if (!startingDayRes.equals("") && !endingDayRes.equals("")) {
                String[] startingDay = startingDayRes.split(";"),
                        endingDay = endingDayRes.split(";");
                taskData = new TaskData(
                        cursor.getInt(0)/*id*/, cursor.getInt(1) == 1/*status*/,
                        cursor.getInt(2)/*category*/, cursor.getInt(3)/*priorities*/,
                        cursor.getInt(4)/*timePriorities*/, cursor.getString(5)/*taskTitle*/,
                        cursor.getString(6)/*taskDetails*/,
                        CalendarDay.from(Integer.parseInt(startingDay[0]),
                                Integer.parseInt(startingDay[1]),
                                Integer.parseInt(startingDay[2]))/*startDate*/,
                        CalendarDay.from(Integer.parseInt(endingDay[0]),
                                Integer.parseInt(endingDay[1]),
                                Integer.parseInt(endingDay[2]))/*endDate*/
                );
            } else {
                taskData = new TaskData(
                        cursor.getInt(0)/*id*/, cursor.getInt(1) == 1/*status*/,
                        cursor.getInt(2)/*category*/, cursor.getInt(3)/*priorities*/,
                        cursor.getInt(4)/*timePriorities*/, cursor.getString(5)/*taskTitle*/,
                        cursor.getString(6)/*taskDetails*/,
                        null/*startDate*/,
                        null/*endDate*/
                );
            }
        } else {
            throw new Exception("STH is wrong");
        }
        cursor.close();
        db.close();
        return taskData;

    }

    /**
     * Method add one taskData to table "TASK_DATA_TABLE"
     *
     * @param taskData which will be add
     * @return true if adding was succeed or false if an error occurred
     * @author Mikołaj Szymczyk
     */
    @Override
    public boolean addOne(TaskData taskData) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        //todo update TaskDataID!
        values.put(COL_CATEGORY, taskData.getCategory().getTaskCategory());
        values.put(COL_STATUS, taskData.getStatus());
        values.put(COL_PRIORITIES, taskData.getPriorities().getPriorityLvl());
        values.put(COL_TIME_PRIORITIES, taskData.getTimePriority().getTimePriorityLvl());
        values.put(COL_TASK_TITLE, taskData.getTaskTitleText());
        values.put(COL_TASK_DETAILS, taskData.getTaskDetailsText());
        values.put(COL_STARTING_DATE, taskData.getStartingDate() == null ? "" :
                taskData.getStartingDate().getDay() + ";" +
                        taskData.getStartingDate().getMonth() + ";" +
                        taskData.getStartingDate().getYear());
        values.put(COL_ENDING_DATE, taskData.getStartingDate() == null ? "" :
                taskData.getEndingDate().getDay() + ";" +
                        taskData.getEndingDate().getMonth() + ";" +
                        taskData.getEndingDate().getYear());
        long insertReturn = db.insert(TAB_TASK_DATA, null, values);
        db.close();
        return !(insertReturn == -1);
    }

    /**
     * @return List of all TaskData
     * @throws Exception if there was a problem with reading
     * @author Mikołaj Szymczyk
     */
    @Override
    public List<TaskData> getTaskData() throws Exception {
        SQLiteDatabase db = database.getReadableDatabase();
        List<TaskData> taskDataList = new ArrayList<>();

        String query = "SELECT * FROM " + TAB_TASK_DATA;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String startingDayRes = cursor.getString(7), endingDayRes = cursor.getString(8);
                if (!startingDayRes.equals("") && !endingDayRes.equals("")) {
                    String[] startingDay = startingDayRes.split(";"),
                            endingDay = endingDayRes.split(";");
                    taskDataList.add(new TaskData(
                            cursor.getInt(0)/*id*/, cursor.getInt(1) == 1/*status*/,
                            cursor.getInt(2)/*category*/, cursor.getInt(3)/*priorities*/,
                            cursor.getInt(4)/*timePriorities*/, cursor.getString(5)/*taskTitle*/,
                            cursor.getString(6)/*taskDetails*/,
                            CalendarDay.from(Integer.parseInt(startingDay[0]),
                                    Integer.parseInt(startingDay[1]),
                                    Integer.parseInt(startingDay[2]))/*startDate*/,
                            CalendarDay.from(Integer.parseInt(endingDay[0]),
                                    Integer.parseInt(endingDay[1]),
                                    Integer.parseInt(endingDay[2]))/*endDate*/
                    ));
                } else {
                    taskDataList.add(new TaskData(
                            cursor.getInt(0)/*id*/, cursor.getInt(1) == 1/*status*/,
                            cursor.getInt(2)/*category*/, cursor.getInt(3)/*priorities*/,
                            cursor.getInt(4)/*timePriorities*/, cursor.getString(5)/*taskTitle*/,
                            cursor.getString(6)/*taskDetails*/,
                            null/*startDate*/,
                            null/*endDate*/
                    ));
                }


            } while (cursor.moveToNext());
        } else {
            throw new Exception("STH is wrong");
        }
        cursor.close();
        db.close();
        return taskDataList;
    }

    @Override
    public List<TaskData> getTaskData(CalendarDay startingDay, CalendarDay calendarDay) {
        //todo implement
        return null;
    }

    @Override
    public List<TaskData> getTaskData(TaskCategory taskCategory) {
        //todo implement
        return null;
    }

    @Override
    public List<TaskData> getTaskData(Priorities priorities, TimePriority timePriority) {
        //todo implement
        return null;
    }

    /**
     * Method delete one taskData from database
     *
     * @param taskData which will be deleted
     * @return true if deleting was succeed
     * @author Mikołaj Szymczyk
     */
    @Override
    public boolean deleteOne(TaskData taskData) {
        SQLiteDatabase db = database.getWritableDatabase();
        String query = "DELETE FROM " + TAB_TASK_DATA + " WHERE " + COL_TASK_DATA_ID + " = " + taskData.getTaskDataId();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     * Method will updating given TaskData
     *
     * @param taskData which will be edited
     * @return true if deleting was succeed
     * @author Mikołaj Szymczyk
     */
    @Override
    public boolean editOne(TaskData taskData) {
        SQLiteDatabase db = database.getWritableDatabase();
        //todo implement
        return false;
    }
}