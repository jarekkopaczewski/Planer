package skills.future.planer.db.task;

import static org.junit.Assert.*;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

public class DayCategorizedTaskDataTest {

//    DayCategorizedTaskData data = new DayCategorizedTaskData();
//
//    @Test
//    public void getAllDaysBetween() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        CalendarDay da1 = CalendarDay.from(2020, 12, 31), day2 = CalendarDay.from(2021, 1, 1);
//        List<CalendarDay> list = new ArrayList<>();
//        list.add(CalendarDay.from(2020, 12, 31));
//        list.add(CalendarDay.from(2021, 1, 1));
//        Method m = data.getClass().getDeclaredMethod("getAllDaysBetween");
//        m.setAccessible(true);
//        m.invoke(data);
//        var result = data.getAllDaysBetween(da1, day2);
//        assertEquals(list.size(), result.size());
//        assertEquals(list, result);
//    }

//    @Test
//    public void run() {
//        List<TaskData> taskData = new ArrayList<>();
//        taskData.add(new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent,
//                "tutul", "", CalendarDay.from(2020, 1, 1),
//                CalendarDay.from(2020, 1, 3)));
//        data.run(taskData);
//        assertEquals(taskData,data.getTaskDataMap().get(CalendarDay.from(2020, 1, 4)).importantUrgentTask);
//    }
}
