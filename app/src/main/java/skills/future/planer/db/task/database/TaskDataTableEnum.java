package skills.future.planer.db.task.database;

import androidx.annotation.NonNull;

public enum TaskDataTableEnum {
    TAB_TASK_DATA("TAB_TASK_DATA", -1),
    COL_TASK_DATA_ID("COL_TASK_DATA_ID", 0),
    COL_STATUS("COL_STATUS", 1),
    COL_CATEGORY("COL_CATEGORY", 2),
    COL_PRIORITIES("COL_PRIORITIES", 3),
    COL_TIME_PRIORITIES("COL_TIME_PRIORITIES", 4),
    COL_TASK_TITLE("COL_TASK_TITLE", 5),
    COL_TASK_DETAILS("COL_TASK_DETAILS", 6),
    COL_STARTING_DATE("COL_STARTING_DATE", 7),
    COL_ENDING_DATE("COL_ENDING_DATE", 8);

    TaskDataTableEnum(String tabTaskData, int i) {

    }


    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
