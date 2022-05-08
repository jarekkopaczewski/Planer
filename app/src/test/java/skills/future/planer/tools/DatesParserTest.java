package skills.future.planer.tools;

import static org.junit.Assert.*;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Test;

import java.time.LocalDate;

public class DatesParserTest {
    Long milliseconds =1641002400000L;// 1640991600000L;

    @Test
    public void toCalendarDay() {
        assertEquals(DatesParser.toCalendarDay(milliseconds),
                CalendarDay.from(2022,1,1));
    }

    @Test
    public void toLocalDate() {
        assertEquals(DatesParser.toLocalDate(milliseconds),
                LocalDate.of(2022,1,1));
    }

    @Test
    public void testToLocalDate() {
        assertEquals(DatesParser.toLocalDate(CalendarDay.from(2022,1,1)),
                LocalDate.of(2022,1,1));
    }

    @Test
    public void toMilliseconds() {
        assertEquals(DatesParser.toMilliseconds( LocalDate.of(2022,1,1)),
                milliseconds);
    }

    @Test
    public void testToMilliseconds() {
        assertEquals(DatesParser.toMilliseconds(CalendarDay.from(2022,1,1)),
                milliseconds);
    }
}