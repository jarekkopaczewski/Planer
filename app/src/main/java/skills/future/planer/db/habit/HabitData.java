package skills.future.planer.db.habit;


import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import skills.future.planer.db.DataBaseException;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;

@Getter
@Setter
@Entity
public class HabitData implements MixedRecyclerElement, Cloneable {
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
    private Long endDay;
    /**
     * string with status if habit was done, 1 -was done, 0 -no,
     * first bit is status for begin day etc.
     */
    private String dayChecking;
    private Long foreignKeyToGoal;
    private Long notificationTime;
    private boolean notification_icon;

    public HabitData() {
        title = dayChecking = daysOfWeek = "";
        habitDuration = HabitDuration.Short;
        beginDay = endDay = 0L;
    }

    public HabitData(String title, String daysOfWeek, HabitDuration habitDuration,
                     LocalDate beginDay, int hour, int minutes) throws Exception {
        this.title = title;
        editDaysOfWeek(daysOfWeek);
        this.habitDuration = habitDuration;
        this.beginDay = DatesParser.toMilliseconds(beginDay);
        this.endDay = DatesParser.toMilliseconds(beginDay.plusDays(habitDuration.getDaysNumber() - 1));
        dayChecking = generate(() -> "0").limit(habitDuration.getDaysNumber()).collect(joining());
        var cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        var cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        this.notificationTime = cal.getTimeInMillis() - cal2.getTimeInMillis();
    }

    public HabitData(String title, String daysOfWeek, HabitDuration habitDuration,
                     LocalDate beginDay, int hour, int minutes, Long foreignKeyToGoal) throws Exception {
        this.title = title;
        editDaysOfWeek(daysOfWeek);
        this.habitDuration = habitDuration;
        this.beginDay = DatesParser.toMilliseconds(beginDay);
        this.endDay = DatesParser.toMilliseconds(beginDay.plusDays(habitDuration.getDaysNumber() - 1));
        dayChecking = generate(() -> "0").limit(habitDuration.getDaysNumber()).collect(joining());
        var cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        var cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        this.notificationTime = cal.getTimeInMillis() - cal2.getTimeInMillis();
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

    public boolean isDayOfWeekChecked(LocalDate date) {
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
        if (dif >= 0 && dif < habitDuration.getDaysNumber())
            return dayChecking.charAt(dif) == '1';
        return false;
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
            }
            endDay = DatesParser.toMilliseconds(DatesParser.toLocalDate(beginDay).plusDays(habitDuration.getDaysNumber() - 1));
            this.habitDuration = habitDuration;
        }
    }

    public void setNotificationTime(int hours, int minutes) {
        var cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        var cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        this.notificationTime = cal.getTimeInMillis() - cal2.getTimeInMillis();
    }

    /**
     * Compares two habitData objects
     * Checking fields except dates
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HabitData habitData = (HabitData) o;

        if (notification_icon != habitData.notification_icon) {return false;}
        if (habitId != null ? !habitId.equals(habitData.habitId) : habitData.habitId != null){return false;}
        if (title != null ? !title.equals(habitData.title) : habitData.title != null){return false;}
        if (daysOfWeek != null ? !daysOfWeek.equals(habitData.daysOfWeek) : habitData.daysOfWeek != null){return false;}
        if (habitDuration != habitData.habitDuration){return false;}
        if (dayChecking != null ? !dayChecking.equals(habitData.dayChecking) : habitData.dayChecking != null){return false;}
        if (foreignKeyToGoal != null ? !foreignKeyToGoal.equals(habitData.foreignKeyToGoal) : habitData.foreignKeyToGoal != null){return false;}
        return true;
    }

    @Override
    public int hashCode() {
        int result = habitId != null ? habitId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (daysOfWeek != null ? daysOfWeek.hashCode() : 0);
        result = 31 * result + (habitDuration != null ? habitDuration.hashCode() : 0);
        result = 31 * result + (beginDay != null ? beginDay.hashCode() : 0);
        result = 31 * result + (endDay != null ? endDay.hashCode() : 0);
        result = 31 * result + (dayChecking != null ? dayChecking.hashCode() : 0);
        result = 31 * result + (foreignKeyToGoal != null ? foreignKeyToGoal.hashCode() : 0);
        result = 31 * result + (notificationTime != null ? notificationTime.hashCode() : 0);
        result = 31 * result + (notification_icon ? 1 : 0);
        return result;
    }

    @NonNull
    @Override
    public HabitData clone() {
        try {
            return (HabitData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
