package skills.future.planer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import skills.future.planer.db.task.database.TaskDataTable;
import skills.future.planer.db.task.database.TaskDataTableEnum;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "planer.db";
    //private final TaskDataTable taskDataTable = new TaskDataTable();

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TaskDataTable.createTaskDataTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TaskDataTableEnum.TAB_TASK_DATA);
        onCreate(db);
    }
}
