package skills.future.planer.db.task;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import lombok.Getter;
import lombok.Setter;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

/**
 * Class encapsulate data for task
 */
@Getter
@Setter
@Entity(tableName = "taskData")
public class TaskData implements Parcelable {
    /**
     * task id
     */
    @PrimaryKey(autoGenerate = true)
    private int taskDataId;
    /**
     * task status
     */
    @ColumnInfo(name = "status")
    private Boolean status;
    /**
     * task category
     */
    @ColumnInfo(name = "category")
    private TaskCategory category;
    /**
     * task priority
     */
    @ColumnInfo(name = "priorities")
    private Priorities priorities;
    /**
     * task time priority
     */
    @ColumnInfo(name = "timePriority")
    private TimePriority timePriority;
    /**
     * task title
     */
    @ColumnInfo(name = "taskTitleText")
    private String taskTitleText;
    /**
     * task details
     */
    @ColumnInfo(name = "taskDetailsText")
    private String taskDetailsText;
    /**
     * starting date of task period
     */
    @ColumnInfo(name = "startingDate")
    private String startingDate = null;
    /**
     * ending date of task period
     */
    @ColumnInfo(name = "endingDate")
    private String endingDate = null;
    /**
     * Field used to pack taskData to bundle it isn't save in database
     */
    @Ignore
    public static final Creator<TaskData> CREATOR = new Creator<>() {
        @Override
        public TaskData createFromParcel(Parcel in) {
            return new TaskData(in);
        }

        @Override
        public TaskData[] newArray(int size) {
            return new TaskData[size];
        }
    };

    /**
     * Constructor of taskData
     * sets taskDataId = 0
     * sets status = false
     */
    @Ignore
    public TaskData() {
        this.taskDataId = 0;
        this.status = false;
    }

    /**
     * Constructor of taskData
     *
     * @param category        could be private of word (take from enum TaskCategory)
     * @param priorities      could be important or unimportant (take from enum Priorities)
     * @param timePriority    could be urgent or not urgent (take from TimePriority)
     * @param taskTitleText   task title
     * @param taskDetailsText task details
     */
    public TaskData(TaskCategory category, Priorities priorities, TimePriority timePriority,
                    String taskTitleText, String taskDetailsText) {
        this.taskDataId = 0;
        this.status = false;
        this.category = category;
        this.priorities = priorities;
        this.timePriority = timePriority;
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
    }


    /**
     * Constructor of taskData
     *
     * @param category        could be private of word (take from enum TaskCategory)
     * @param priorities      could be important or unimportant (take from enum Priorities)
     * @param timePriority    could be urgent or not urgent (take from TimePriority)
     * @param taskTitleText   task title
     * @param taskDetailsText task details
     * @param startingDate    starting date of task period
     * @param endingDate      ending date of task period
     */
    @Ignore
    public TaskData(TaskCategory category, Priorities priorities, TimePriority timePriority,
                    String taskTitleText, String taskDetailsText, CalendarDay startingDate, CalendarDay endingDate) {
        this.taskDataId = 0;
        this.status = false;
        this.category = category;
        this.priorities = priorities;
        this.timePriority = timePriority;
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
        setEndingCalendarDate(endingDate);
        setStartingCalendarDate(startingDate);
    }

    /**
     * Constructor of taskData used to unpack taskData from bundle
     *
     * @param in packed taskData
     */
    @Ignore
    protected TaskData(Parcel in) {
        taskDataId = in.readInt();
        byte tmpStatus = in.readByte();
        status = tmpStatus == 0 ? null : tmpStatus == 1;
        taskTitleText = in.readString();
        taskDetailsText = in.readString();
        startingDate = in.readString();
        endingDate = in.readString();
    }


    /**
     * Setter of endingDate
     *
     * @param endingCalendarDay date to set
     */
    @Ignore
    public void setEndingCalendarDate(CalendarDay endingCalendarDay) {
        endingDate = endingCalendarDay.getDay() + "." + endingCalendarDay.getMonth() + "."
                + endingCalendarDay.getYear();
    }

    /**
     * Setter of startingDate
     *
     * @param startingCalendarDay date to set
     */
    @Ignore
    public void setStartingCalendarDate(CalendarDay startingCalendarDay) {
        startingDate = startingCalendarDay.getDay() + "." + startingCalendarDay.getMonth() + "."
                + startingCalendarDay.getYear();
    }

    /**
     * Getter of endingDate
     *
     * @return if is set then return date if not then return null
     */
    @Ignore
    public CalendarDay getEndingCalendarDate() {
        if (endingDate != null) {
            String[] strings = endingDate.split("\\.");
            return CalendarDay.from(Integer.parseInt(strings[2]), Integer.parseInt(strings[1]),
                    Integer.parseInt(strings[0]));
        }
        return null;
    }

    /**
     * Getter of starting date
     *
     * @return if is set then return date if not then return null
     */
    @Ignore
    public CalendarDay getStartingCalendarDate() {
        if (startingDate != null) {
            String[] strings = startingDate.split("\\.");
            return CalendarDay.from(Integer.parseInt(strings[2]), Integer.parseInt(strings[1]), Integer.parseInt(strings[0]));
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method write task data to parcel
     *
     * @param dest  destinatn parcel
     * @param flags ?
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskDataId);
        dest.writeByte((byte) (status == null ? 0 : status ? 1 : 2));
        dest.writeString(taskTitleText);
        dest.writeString(taskDetailsText);
        dest.writeString(startingDate);
        dest.writeString(endingDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskData taskData = (TaskData) o;

        if (getTaskDataId() != taskData.getTaskDataId()) return false;
        if (getStatus() != null)
            if (!getStatus().equals(taskData.getStatus())) return false;
        if (getCategory() != taskData.getCategory()) return false;
        if (getPriorities() != taskData.getPriorities()) return false;
        if (getTimePriority() != taskData.getTimePriority()) return false;
        if (getTaskTitleText() != null ? !getTaskTitleText().equals(taskData.getTaskTitleText())
                : taskData.getTaskTitleText() != null)
            return false;
        if (getTaskDetailsText() != null ? !getTaskDetailsText().equals(taskData.getTaskDetailsText())
                : taskData.getTaskDetailsText() != null)
            return false;
        if (getStartingDate() != null ? !getStartingDate().equals(taskData.getStartingDate())
                : taskData.getStartingDate() != null)
            return false;
        return getEndingDate() != null ? getEndingDate().equals(taskData.getEndingDate())
                : taskData.getEndingDate() == null;
    }

    @Override
    @Ignore
    public int hashCode() {
        int result = getTaskDataId();
        if (getStatus() != null)
            result = 31 * result + getStatus().hashCode();
        result = 31 * result + getCategory().hashCode();
        result = 31 * result + getPriorities().hashCode();
        result = 31 * result + getTimePriority().hashCode();
        result = 31 * result + (getTaskTitleText() != null ? getTaskTitleText().hashCode() : 0);
        result = 31 * result + (getTaskDetailsText() != null ? getTaskDetailsText().hashCode() : 0);
        result = 31 * result + (getStartingDate() != null ? getStartingDate().hashCode() : 0);
        result = 31 * result + (getEndingDate() != null ? getEndingDate().hashCode() : 0);
        return result;
    }

    /**
     * Sets all data without dates
     *
     * @param category        could be private or work (take from enum TaskCategory)
     * @param priorities      could be important or unimportant (take from enum Priorities)
     * @param timePriority    could be urgent or not urgent (take from TimePriority)
     * @param taskTitleText   task title
     * @param taskDetailsText task details
     */
    @Ignore
    public void setAllDataWithoutDates(TaskCategory category, Priorities priorities, TimePriority timePriority,
                                       String taskTitleText, String taskDetailsText) {
        this.category = category;
        this.priorities = priorities;
        this.timePriority = timePriority;
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
    }
}
