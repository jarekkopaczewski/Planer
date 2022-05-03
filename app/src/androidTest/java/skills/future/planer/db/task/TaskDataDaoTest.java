package skills.future.planer.db.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

public class TaskDataDaoTest {
    private TaskDataDao userDao;
    private AppDatabase db;
    private TaskData taskData,taskData2;
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        userDao = db.taskDataTabDao();
        taskData = new TaskData(TaskCategory.Private, Priorities.Important,
                TimePriority.Urgent, "Title","Details");
        taskData.setTaskDataId(1);
        taskData2 = new TaskData(TaskCategory.Private, Priorities.Important,
                TimePriority.Urgent, "Title","Details",
                CalendarDay.from(2000,1,1),CalendarDay.from(2020,2,2));
        taskData2.setTaskDataId(2);
        userDao.insert(taskData);
        userDao.insert(taskData2);
    }

    @After
    public void tearDown(){
        db.close();
    }

    @Test
    public void findById() throws Exception {
        assertEquals(taskData, userDao.findById(taskData.getTaskDataId()));
    }

    @Test
    public void insert() throws Exception {
        TaskData taskData1 = new TaskData(TaskCategory.Private, Priorities.NotImportant,
                TimePriority.NotUrgent, "Title","Details");
        taskData1.setTaskDataId(3);
        userDao.insert(taskData1);
        TaskData res = userDao.findById(taskData1.getTaskDataId());
        assertEquals(taskData1,res);
    }

    @Test
    public void getTaskData() {
        List<TaskData> exp = new ArrayList<>(Arrays.asList(taskData,taskData2));
        assertEquals(exp,userDao.getTaskData().getValue());
    }

    @Test
    public void testGetTaskData() throws Exception {
        List<TaskData> exp = new ArrayList<>(Arrays.asList(taskData,taskData2));
        assertEquals(exp,userDao.getTaskData(TaskCategory.Private));
    }

    @Test
    public void testGetTaskData1() throws Exception {
        List<TaskData> exp = new ArrayList<>(Arrays.asList(taskData,taskData2));
        assertEquals(exp,userDao.getTaskData(Priorities.Important,TimePriority.Urgent));
    }

    @Test
    public void deleteOne() {
        userDao.deleteOne(taskData);
        List<TaskData> exp = new ArrayList<>(Collections.singletonList(taskData2));
        assertEquals(exp,userDao.getTaskData().getValue());
    }

    @Test
    public void deleteAll() {
        userDao.deleteAll();
        assertTrue(Objects.requireNonNull(userDao.getTaskData().getValue()).isEmpty());
    }

    @Test
    public void editOne() {
    }

    @Test
    public void getIdOfLastAddedTask() {
        assertEquals(taskData2.getTaskDataId(),userDao.getIdOfLastAddedTask());
    }
}