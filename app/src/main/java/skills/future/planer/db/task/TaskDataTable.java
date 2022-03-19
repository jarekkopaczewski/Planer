package skills.future.planer.db.task;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class TaskDataTable {
    public static final String TAB_TASK_DATA = "TASK_DATA_TABLE";
    public static final String TASK_DATA_ID = "TASK_DATA_ID";
    public static final String COL_STATUS = "COL_STATUS";
    public static final String COL_CATEGORY = "COL_CATEGORY";
    public static final String COL_PRIORITIES = "COL_PRIORITIES";
    public static final String COL_TIME_PRIORITIES = " COL_TIME_PRIORITIES";
    public static final String COL_TASK_TITLE = "COL_TASK_TITLE";
    public static final String COL_TASK_DETAILS = "COL_TASK_DETAILS";
    public static final String COL_STARTING_DATE = "COL_STARTING_DATE";
    public static final String COL_ENDING_DATE = "COL_ENDING_DATE";

    public TaskDataTable() {
    }

    public void createTaskDataTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TAB_TASK_DATA + " (" + TASK_DATA_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_STATUS + " BOOL ," + COL_CATEGORY +
                "  INTEGER, " + COL_PRIORITIES + " INTEGER, " + COL_TIME_PRIORITIES + " INTEGER, " +
                COL_TASK_TITLE + " TEXT," + COL_TASK_DETAILS + " TEXT," +
                COL_STARTING_DATE + " TEXT, " + COL_ENDING_DATE + " TEXT)";
        db.execSQL(query);
    }

    public boolean addOne(TaskData taskData,SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY, taskData.getCategory().getTaskCategory());
        values.put(COL_STATUS, taskData.getStatus());
        values.put(COL_PRIORITIES, taskData.getPriorities().getPriorityLvl());
        values.put(COL_TIME_PRIORITIES, taskData.getTimePriority().getTimePriorityLvl());
        values.put(COL_TASK_TITLE, taskData.getTaskTitleText());
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

    public List<TaskData> getTaskData(SQLiteDatabase db) throws Exception {
        List<TaskData> taskDataList = new ArrayList<>();

        String query = "SELECT * FROM " + TAB_TASK_DATA;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String startingDayRes = cursor.getString(6),endingDayRes = cursor.getString(7) ;
                if(startingDayRes!= null && endingDayRes!= null) {
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
                }
                else {
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
}