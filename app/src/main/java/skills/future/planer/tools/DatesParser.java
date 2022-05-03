package skills.future.planer.tools;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DatesParser {
    public static CalendarDay toCalendarDay(Long dateInMilliseconds) {
        var date = Instant.ofEpochMilli(dateInMilliseconds)
                .atZone(ZoneId.systemDefault()).toLocalDate();

        return  CalendarDay.from(date.getYear(),date.getMonth().getValue(),date.getDayOfMonth());
    }

    public static LocalDate toLocalDate(Long dateInMilliseconds) {
        var date = Instant.ofEpochMilli(dateInMilliseconds)
                .atZone(ZoneId.systemDefault()).toLocalDate();
        return LocalDate.of(date.getYear(), date.getMonth(),
                date.getDayOfMonth());
    }

    public static LocalDate toLocalDate(CalendarDay calendarDay) {
        return LocalDate.of(calendarDay.getYear(),calendarDay.getMonth(),
                calendarDay.getDay());
    }

    public static Long toMilliseconds(LocalDate localDate) {

        return java.util.Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.of("Etc/GMT+2"))
                .toInstant()).getTime();
    }

    public static Long toMilliseconds(CalendarDay calendarDay) {
        return DatesParser.toMilliseconds(LocalDate.of(calendarDay.getYear(),calendarDay.getMonth(),
                calendarDay.getDay()));
    }
}