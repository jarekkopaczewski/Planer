package skills.future.planer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataTable;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "planer.db";
    private final TaskDataTable taskDataTable = new TaskDataTable();

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        taskDataTable.createTaskDataTable(db);
    }

    private void createTaskDataTable(SQLiteDatabase db) {
        taskDataTable.createTaskDataTable(db);
    }

    public boolean addOne(TaskData taskData) {
        return taskDataTable.addOne(taskData,this.getWritableDatabase());
    }

    public List<TaskData> getTaskData() throws Exception {
        return taskDataTable.getTaskData(this.getReadableDatabase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TaskDataTable.TAB_TASK_DATA);
        onCreate(db);
    }
}
