package skills.future.planer.db.habit;


import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import skills.future.planer.db.DataBaseException;
import skills.future.planer.tools.DatesParser;

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
    private Long beginDay;
    @Getter(AccessLevel.PACKAGE)
    private Long endDay;
    /**
     * string with status if habit was done, 1 -was done, 0 -no,
     * first bit is status for begin day etc.
     */
    private String dayChecking;
    private Long foreignKeyToGoal;
    private Long notificationTime;

    public HabitData() {
        title = dayChecking = daysOfWeek = "";
        habitDuration = HabitDuration.Short;
        beginDay = endDay = 0L;
    }

    public HabitData(String title, String daysOfWeek, HabitDuration habitDuration,
                     LocalDate beginDay, Calendar calendar) throws Exception {
        this.title = title;
        editDaysOfWeek(daysOfWeek);
        this.habitDuration = habitDuration;
        this.beginDay = DatesParser.toMilliseconds(beginDay);
        this.endDay = DatesParser.toMilliseconds(beginDay.plusDays(habitDuration.getDaysNumber() - 1));
        dayChecking = generate(() -> "0").limit(habitDuration.getDaysNumber()).collect(joining());
        notificationTime = calendar.getTimeInMillis();
    }

    public HabitData(String title, String daysOfWeek, HabitDuration habitDuration,
                     LocalDate beginDay, Calendar calendar, Long foreignKeyToGoal) throws Exception {
        this.title = title;
        editDaysOfWeek(daysOfWeek);
        this.habitDuration = habitDuration;
        this.beginDay = DatesParser.toMilliseconds(beginDay);
        this.endDay = DatesParser.toMilliseconds(beginDay.plusDays(habitDuration.getDaysNumber() - 1));
        dayChecking = generate(() -> "0").limit(habitDuration.getDaysNumber()).collect(joining());
        notificationTime = calendar.getTimeInMillis();
        this.foreignKeyToGoal = foreignKeyToGoal;
    }

    /**
     * @return list of calendar days where habit was done
     */
    public List<CalendarDay> getAllDaysWhereHabitsWasDone() {
        var dateIterator = DatesParser.toLocalDate(beginDay);
        List<CalendarDay> result = new ArrayList<>();
        for (int i = 0; i < habitDuration.getDaysNumber(); i++) {
            if (dayChecking.charAt(i) == '1' && isDayOfWeekChecked(dateIterator))
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
            if (dayChecking.charAt(i) == '1' && isDayOfWeekChecked(dateIterator))
                result++;
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
            if (dayChecking.charAt(i) == '0' && isDayOfWeekChecked(dateIterator))
                result++;
            dateIterator = dateIterator.plusDays(1);
        }
        return result;
    }

    private boolean isDayOfWeekChecked(LocalDate date) {
        StringBuilder myName = new StringBuilder(getDaysOfWeek());
        return switch (date.getDayOfWeek()) {
            case MONDAY -> myName.charAt(0) == '1';
            case TUESDAY -> myName.charAt(1) == '1';
            case WEDNESDAY -> myName.charAt(2) == '1';
            case THURSDAY -> myName.charAt(3) == '1';
            case FRIDAY -> myName.charAt(4) == '1';
            case SATURDAY -> myName.charAt(5) == '1';
            case SUNDAY -> myName.charAt(6) == '1';
        };
    }

    public String getDayWeekName(int i) {
        return switch (i) {
            case 0 -> "Pn";
            case 1 -> "Wt";
            case 2 -> "Sr";
            case 3 -> "Czw";
            case 4 -> "Pt";
            case 5 -> "So";
            case 6 -> "Nie";
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }

    public CalendarDay getBeginCalendarDay() {
        return beginDay != 0 ? DatesParser.toCalendarDay(beginDay) : null;
    }

    public CalendarDay getEndCalendarDay() {
        return endDay != 0 ? DatesParser.toCalendarDay(endDay) : null;
    }

    public Calendar getNotificationTimeCalendar() {
        var cal = Calendar.getInstance();
        cal.setTimeInMillis(notificationTime);
        return cal;
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
        int dif = DatesParser.countDifferenceBetweenDays(DatesParser.toCalendarDay(beginDay), calendarDay);
        if (dif > habitDuration.getDaysNumber())
            throw new DataBaseException("tst");
        if (dayChecking.charAt(dif) == '0')
            myName.setCharAt(dif, '1');
        else
            myName.setCharAt(dif, '0');
        setDayChecking(myName.toString());
    }

    public void editDaysOfWeek(String daysOfWeek) throws Exception {
        if (daysOfWeek.length() != 7)
            throw new DataBaseException("Wrong daysOfWeek argument length. Excepted: 7, Actual:" +
                    daysOfWeek.length());
        this.daysOfWeek = daysOfWeek;
    }

    public boolean isHabitDone(CalendarDay globalSelectedDate) {
        int dif = (int) ChronoUnit.DAYS.between(DatesParser.toLocalDate(beginDay),
                DatesParser.toLocalDate(globalSelectedDate));
        return dayChecking.charAt(dif) == '1';
    }

    public void setBeginLocalDay(LocalDate beginDay) {
        this.beginDay = DatesParser.toMilliseconds(beginDay);
        this.endDay = DatesParser.toMilliseconds(beginDay.plusDays(habitDuration.getDaysNumber() - 1));
    }

    public void editHabitDur(HabitDuration habitDuration) {
        if (!this.habitDuration.getDaysNumber().equals(habitDuration.getDaysNumber())) {
            if (this.habitDuration.getDaysNumber() < habitDuration.getDaysNumber()) {
                dayChecking = dayChecking + generate(() -> "0").limit(habitDuration.getDaysNumber() -
                        this.habitDuration.getDaysNumber()).collect(joining());
            } else {
                dayChecking = dayChecking.substring(0, habitDuration.getDaysNumber());
                //todo zmienić końcowy dzień
            }
            this.habitDuration = habitDuration;
        }
    }
}
