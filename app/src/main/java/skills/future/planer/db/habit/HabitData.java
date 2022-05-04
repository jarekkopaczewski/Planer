package skills.future.planer.db.habit;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import skills.future.planer.db.DataBaseException;
import skills.future.planer.tools.DatesParser;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

@Getter
@Setter
@Entity
public class HabitData {
    @PrimaryKey(autoGenerate = true)
    private Long habitId;
    private String title;
    /**
     * 7 bits, first bit is monday, last sunday, 1-habit is doing in this day, 0 opposite
     */
    private String daysOfWeek;
    /**
     * days number
     */
    private HabitDuration habitDuration;
    private Long beginDay, endDay;
    /**
     * string with status if habit was done, 1 -was done, 0 -no,
     * first bit is status for begin day etc.
     */
    @Getter(AccessLevel.PACKAGE)
    private String dayChecking;

    HabitData() {
        habitId = 0L;
        title = dayChecking = daysOfWeek = "";
        habitDuration = HabitDuration.Short;
        beginDay = endDay = 0L;
    }

    @Ignore
    public HabitData(String title, String daysOfWeek, HabitDuration habitDuration,
                     LocalDate beginDay) throws DataBaseException {
        this.habitId = 0L;
        this.title = title;
        setDaysOfWeek(daysOfWeek);
        this.habitDuration = habitDuration;
        this.beginDay =  DatesParser.toMilliseconds(beginDay);
        this.endDay = DatesParser.toMilliseconds(beginDay.plusDays(habitDuration.getDaysNumber()-1));
        dayChecking = generate(() -> "0").limit(habitDuration.getDaysNumber()).collect(joining());
    }

    /**
     * @return list of calendar days where habit was done
     */
    public List<CalendarDay> getAllDaysWhereHabitsWasDone() {
        var dateIterator = DatesParser.toLocalDate(beginDay);
        List<CalendarDay> result = new ArrayList<>();
        for (int i = 0; i < habitDuration.getDaysNumber(); i++) {
            if (dayChecking.charAt(i) == '1'&& isDayOfWeekChecked(dateIterator))
                result.add(DatesParser.toCalendarDay(dateIterator));
            dateIterator = dateIterator.plusDays(1);
        }
        return result;
    }

    /**
     * @return list of calendar days where habit was done
     */
    public int getNumberOfDaysWhereHabitsWasDone() {
        var dateIterator = DatesParser.toLocalDate(beginDay);
        var result = 0;
        for (int i = 0; i < habitDuration.getDaysNumber(); i++) {
            if (dayChecking.charAt(i) == '1'&& isDayOfWeekChecked(dateIterator))
                i++;
            dateIterator = dateIterator.plusDays(1);
        }
        return result;
    }

    /**
     * @return list of calendar days where habit wasn't done
     */
    public int getNumberOfDaysWhereHabitsWasFailure() {
        var dateIterator = DatesParser.toLocalDate(beginDay);
        var result = 0;
        for (int i = 0; i < habitDuration.getDaysNumber(); i++) {
            if (dayChecking.charAt(i) == '0'&& isDayOfWeekChecked(dateIterator))
                i++;
            dateIterator = dateIterator.plusDays(1);
        }
        return result;
    }

    private boolean isDayOfWeekChecked(LocalDate date){
        StringBuilder myName = new StringBuilder(getDaysOfWeek());
        return switch (date.getDayOfWeek()){
            case MONDAY -> myName.charAt(0)=='1';
            case TUESDAY -> myName.charAt(1)=='1';
            case WEDNESDAY -> myName.charAt(2)=='1';
            case THURSDAY -> myName.charAt(3)=='1';
            case FRIDAY -> myName.charAt(4)=='1';
            case SATURDAY -> myName.charAt(5)=='1';
            case SUNDAY -> myName.charAt(6)=='1';
        };

    }

    public CalendarDay getBeginCalendarDay() {
        return beginDay != 0 ? DatesParser.toCalendarDay(beginDay) : null;
    }

    public CalendarDay getEndCalendarDay() {
        return endDay != 0 ? DatesParser.toCalendarDay(endDay) : null;
    }

    /**
     * Method set for givenDay opposite state of accomplish habit
     *
     * @param calendarDay for which state is changing
     */
    public void setHabitDoneIn(CalendarDay calendarDay) throws DataBaseException {
        if (!habitDuration.getDaysNumber().equals(getDayChecking().length()))
            throw new DataBaseException("Habit day checking not set!");
        if (calendarDay.isAfter(getEndCalendarDay()) || calendarDay.isBefore(getBeginCalendarDay()))
            throw new DataBaseException("Wrong calendarDay");
        StringBuilder myName = new StringBuilder(getDayChecking());
        int dif =(int) ChronoUnit.DAYS.between(DatesParser.toLocalDate(beginDay),
                        DatesParser.toLocalDate(calendarDay));
        if( dif >habitDuration.getDaysNumber())
            throw new DataBaseException("tst");
        if (dayChecking.charAt(dif) == '0')
            myName.setCharAt(dif, '1');
        else
            myName.setCharAt(dif, '0');
        setDayChecking(myName.toString());
    }

    public void setDaysOfWeek(String daysOfWeek) throws DataBaseException {
        if (daysOfWeek.length() != 7)
            throw new DataBaseException("Wrong daysOfWeek argument length. Excepted: 7, Actual:" +
                    daysOfWeek.length());
        this.daysOfWeek = daysOfWeek;
    }

}
