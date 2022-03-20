package skills.future.planer.db.task;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskDataTableTest {
    private TaskDataTable taskDataTable;


    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        taskDataTable = new TaskDataTable(appContext);
    }

    @Test
    public void createTaskDataTable() {
    }

    @Test
    public void addOne() {
        TaskData taskData = new TaskData(TaskCategory.Work,Priorities.Important,TimePriority.Urgent,
                "title1","details1");
        TaskData taskData1 = new TaskData(TaskCategory.Private,Priorities.Important,
                TimePriority.NotUrgent, "title2","details2",
                CalendarDay.from(2000,1,31), CalendarDay.from(2000,3,31));
       taskDataTable.addOne(taskData);
    }

    @Test
    public void getTaskData() {
    }

    @Test
    public void deleteOne() {
    }

    @Test
    public void findById() {
        try {
            TaskData taskData = taskDataTable.findById(1);
            Assert.assertEquals(TaskCategory.Private, taskData.getCategory());
            Assert.assertEquals(Priorities.Important,taskData.getPriorities());
            Assert.assertEquals(TimePriority.Urgent,taskData.getTimePriority());
            Assert.assertEquals(Priorities.Important,taskData.getPriorities());
            Assert.assertEquals(false,taskData.getStatus());
            Assert.assertEquals("USB",taskData.getTaskTitleText());
            Assert.assertEquals("such",taskData.getTaskDetailsText());
            Assert.assertNull(taskData.getStartingDate());
            Assert.assertNull(taskData.getEndingDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddOne() {
    }

    @Test
    public void testGetTaskData() {
    }

    @Test
    public void testGetTaskData1() {
    }

    @Test
    public void testGetTaskData2() {
    }

    @Test
    public void testGetTaskData3() {
    }

    @Test
    public void testDeleteOne() {
    }

    @Test
    public void editOne() {
    }
}