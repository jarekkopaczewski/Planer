package skills.future.planer.db.task.database;


import org.junit.Assert;
import org.junit.Test;

public class TaskDataTableEnumTest {


    @Test
    public void testTestToString() {
        Assert.assertEquals("COL_STARTING_DATE", ""+TaskDataTableEnum.COL_STARTING_DATE);

    }
}