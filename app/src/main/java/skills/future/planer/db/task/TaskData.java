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

@Getter
@Setter
@Entity(tableName = "taskData")
public class TaskData implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int taskDataId;
    @ColumnInfo(name = "status")
    private Boolean status;
    @ColumnInfo(name = "category")
    private TaskCategory category;
    @ColumnInfo(name = "priorities")
    private Priorities priorities;
    @ColumnInfo(name = "timePriority")
    private TimePriority timePriority;
    @ColumnInfo(name = "taskTitleText")
    private String taskTitleText;
    @ColumnInfo(name = "taskDetailsText")
    private String taskDetailsText;
    @ColumnInfo(name = "startingDate")
    private String startingDate = null;
    @ColumnInfo(name = "endingDate")
    private String endingDate = null;

    public TaskData(int taskDataId, Boolean status, int category, int priorities,
                    int timePriority, String taskTitleText,String taskDetailsText,
                    CalendarDay startingDate, CalendarDay endingDate) {
        this.taskDataId = taskDataId;
        this.status = status;
        switch (category) {
            case 0 -> this.category = TaskCategory.Private;
            case 1 -> this.category = TaskCategory.Work;
        }
        switch (priorities) {
            case 0 -> this.priorities = Priorities.Important;
            case 1 -> this.priorities = Priorities.NotImportant;
        }
        switch (timePriority) {
            case 0 -> this.timePriority = TimePriority.Urgent;
            case 1 -> this.timePriority = TimePriority.NotUrgent;
        }
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
        setEndingCalendarDate(endingDate);
        setStartingCalendarDate(startingDate);
    }

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

    @Ignore
    public TaskData(TaskCategory category, Priorities priorities, TimePriority timePriority,
                    String taskTitleText, String taskDetailsText, CalendarDay startingDate, CalendarDay endingDate) {
        this.category = category;
        this.priorities = priorities;
        this.timePriority = timePriority;
        this.taskTitleText = taskTitleText;
        this.taskDetailsText = taskDetailsText;
        setEndingCalendarDate(endingDate);
        setStartingCalendarDate(startingDate);
    }

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

    @Ignore
    public void setEndingCalendarDate(CalendarDay endingCalendarDay) {
        endingDate = endingCalendarDay.getDay()+"."+endingCalendarDay.getMonth()+"."+endingCalendarDay.getYear();
    }


    @Ignore
    public void setStartingCalendarDate(CalendarDay startingCalendarDay) {
        startingDate = startingCalendarDay.getDay()+"."+startingCalendarDay.getMonth()+"."+startingCalendarDay.getYear();
    }

    @Ignore
    public CalendarDay getEndingCalendarDate() {
        if (endingDate != null) {
            String[] strings = endingDate.split("\\.");
            return CalendarDay.from(Integer.parseInt(strings[1]),Integer.parseInt(strings[1]),Integer.parseInt(strings[0]));
        }
        return null;
    }

    @Ignore
    public CalendarDay getStartingCalendarDate() {
        if (startingDate != null) {
            String[] strings = startingDate.split("\\.");
            return CalendarDay.from(Integer.parseInt(strings[1]),Integer.parseInt(strings[1]),Integer.parseInt(strings[0]));
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskDataId);
        dest.writeByte((byte) (status == null ? 0 : status ? 1 : 2));
        dest.writeString(taskTitleText);
        dest.writeString(taskDetailsText);
        dest.writeString(startingDate);
        dest.writeString(endingDate);
    }
}
