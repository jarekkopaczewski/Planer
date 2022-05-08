package skills.future.planer.tools;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        return LocalDate.of(calendarDay.getYear(), calendarDay.getMonth(),
                calendarDay.getDay());
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Long toMilliseconds(LocalDate localDate) {
        return java.util.Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.of("Etc/GMT+1"))
                .toInstant()).getTime();
    }

    public static Long toMilliseconds(CalendarDay calendarDay) {
        return DatesParser.toMilliseconds(LocalDate.of(calendarDay.getYear(), calendarDay.getMonth(),
                calendarDay.getDay()));
    }

    public static CalendarDay toCalendarDay(LocalDate localDate) {
        return CalendarDay.from(localDate.getYear(), localDate.getMonth().getValue(), localDate.getDayOfMonth());
    }

    public static int countDifferenceBetweenDays(CalendarDay begin, CalendarDay end) {
        return (int) ChronoUnit.DAYS.between(DatesParser.toLocalDate(begin),
                DatesParser.toLocalDate(end));
    }

    public static Calendar toCalendar(Long date) {
        var cal = Calendar.getInstance();
        var loc = toLocalDate(date);
        cal.clear();
        cal.set(loc.getYear(), loc.getMonthValue(), loc.getDayOfMonth());
        return cal;
    }

    public static String toSting(CalendarDay day) {
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        var cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day.getDay());
        cal.set(Calendar.MONTH, day.getMonth() - 1);
        cal.set(Calendar.YEAR, day.getYear());
        return formatterDate.format(cal.getTime());
    }
}
