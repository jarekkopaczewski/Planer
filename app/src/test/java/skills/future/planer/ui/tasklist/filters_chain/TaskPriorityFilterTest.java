package skills.future.planer.ui.tasklist.filters_chain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;

public class TaskPriorityFilterTest {
    List<TaskData> fullList;
    TaskData t1, t2, t3, t4, t5, t6, t7, t8;
    BaseFilter filter;

    @Before
    public void setUp() {
        t1 = new TaskData(TaskCategory.Private, Priorities.Important, TimePriority.Urgent, "TitleT1", "det");
        t2 = new TaskData(TaskCategory.Private, Priorities.Important, TimePriority.NotUrgent, "TitleT1", "det");
        t3 = new TaskData(TaskCategory.Private, Priorities.NotImportant, TimePriority.Urgent, "TitleT1", "det");
        t4 = new TaskData(TaskCategory.Private, Priorities.NotImportant, TimePriority.NotUrgent, "TitleT1", "det");
        t5 = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.Urgent, "TitleT1", "det");
        t6 = new TaskData(TaskCategory.Work, Priorities.Important, TimePriority.NotUrgent, "TitleT1", "det");
        t7 = new TaskData(TaskCategory.Work, Priorities.NotImportant, TimePriority.Urgent, "TitleT1", "det");
        t8 = new TaskData(TaskCategory.Work, Priorities.NotImportant, TimePriority.NotUrgent, "TitleT1", "det");
        fullList = List.of(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Test
    public void validateOnlyPriority() {
        filter = new TaskPriorityFilter(Priorities.Important);
        var list1 = List.of(t1, t2, t5, t6);
        var list2 = filter.filter(fullList);
        assertEquals(list1, list2);
        assertNotSame(list1, list2);
    }

    @Test
    public void validateOnlyTimePriority() {
        filter = new TaskTimePriorityFilter(TimePriority.Urgent);
        var list1 = List.of(t1, t3, t5, t7);
        var list2 = filter.filter(fullList);
        assertEquals(list1, list2);
        assertNotSame(list1, list2);
    }

    @Test
    public void validateTimePriorityAndPriority() {
        filter = new TaskPriorityFilter(Priorities.Important);
        filter.setNext(new TaskTimePriorityFilter(TimePriority.Urgent));
        var list1 = List.of(t1, t5);
        var list2 = filter.filter(fullList);
        list2.forEach(taskData -> System.out.println(taskData.getPriorities() + "," + taskData.getTimePriority()));
        assertEquals(list1, list2);
        assertNotSame(list1, list2);
    }
}