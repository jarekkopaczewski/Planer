package skills.future.planer.db.task;

import static org.junit.Assert.*;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Before;
import org.junit.Test;

import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

public class TaskDataTest {
    TaskData taskData;
    @Before
    public void setUp() {
        taskData = new TaskData(TaskCategory.Private, Priorities.Important,
                TimePriority.Urgent, "Title","Details",
                CalendarDay.from(2000,1,1),
                CalendarDay.from(2000,1,2));
    }

    @Test
    public void setEndingCalendarDate() {
        assertEquals("2.1.2000",taskData.getEndingDate());
        taskData.setEndingCalendarDate(CalendarDay.from(2000,2,2));
        assertEquals("2.2.2000",taskData.getEndingDate());
    }

    @Test
    public void setStartingCalendarDate() {
        assertEquals("1.1.2000",taskData.getStartingDate());
        taskData.setStartingCalendarDate(CalendarDay.from(2000,2,1));
        assertEquals("1.2.2000",taskData.getStartingDate());
    }

    @Test
    public void getEndingCalendarDate() {
        assertEquals(CalendarDay.from(2000,1,2),taskData.getEndingCalendarDate());
    }

    @Test
    public void getStartingCalendarDate() {
        assertEquals(CalendarDay.from(2000,1,1),taskData.getStartingCalendarDate());

    }

    @Test
    public void testEquals() {
        TaskData taskData2 = new TaskData(TaskCategory.Private, Priorities.Important,
                TimePriority.Urgent, "Title","Details",
                CalendarDay.from(2000,1,1),
                CalendarDay.from(2000,1,2));
        assertEquals(taskData2, taskData);
        assertEquals(taskData, taskData2);
    }

    @Test
    public void testNotEquals() {
        TaskData taskData2 = new TaskData(TaskCategory.Work, Priorities.Important,
                TimePriority.Urgent, "Title","Details",
                CalendarDay.from(2000,1,1),
                CalendarDay.from(2000,1,2));
        assertNotEquals(taskData2, taskData);
        assertNotEquals(taskData, taskData2);
    }

    @Test
    public void testHashCode() {
        TaskData taskData2 = new TaskData(TaskCategory.Private, Priorities.Important,
                TimePriority.Urgent, "Title","Details",
                CalendarDay.from(2000,1,1),
                CalendarDay.from(2000,1,2));
        assertEquals(taskData2.hashCode(), taskData.hashCode());
        assertEquals(taskData.hashCode(), taskData2.hashCode());
    }
}