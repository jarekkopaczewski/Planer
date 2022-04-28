package skills.future.planer.db.habit;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
    private Integer daysOfWeek;
    /**
     * days number
     */
    private HabitDuration habitDuration;
    private Long beginDay, endDay;
    @Getter(AccessLevel.PACKAGE)
    private Long dayCheckingPt1, dayCheckingPt2;

    public HabitData() {
        habitId = 0L;
        title = "";
        daysOfWeek = 0;
        habitDuration = HabitDuration.Short;
        beginDay = endDay = 0L;
        dayCheckingPt1 = dayCheckingPt2 = 0L;
    }

    public HabitData(String title, String daysOfWeek, HabitDuration habitDuration, Long beginDay) {
        this.habitId = 0L;
        this.title = title;
        this.daysOfWeek = Integer.parseInt(daysOfWeek);
        this.habitDuration = habitDuration;
        this.beginDay = beginDay;
        //todo
        dayCheckingPt1 = dayCheckingPt2 = 0L;
    }

    /**
     * Method sets new days of week
     * @param daysOfWeek method set on which days of week habit will be processing
     */
    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = Integer.parseInt(daysOfWeek);
    }

    /**
     * @return list of calendar days where habit was done
     */
    public List<CalendarDay> getAllDaysWhereHabitsWasDone() {
        //todo nie licz dni tygodnia nie zaznaczonych w daysOfWeek
        return null;
    }

    /**
     * @return list of calendar days where habit was done
     */
    public int getNumberOfDaysWhereHabitsWasDone() {
        //todo nie licz dni tygodnia nie zaznaczonych w daysOfWeek
        return 0;
    }

    /**
     * @return list of calendar days where habit wasn't done
     */
    public int getNumberOfDaysWhereHabitsWasFailure() {
        //todo nie licz dni tygodnia nie zaznaczonych w daysOfWeek
        return 0;
    }

    /**
     * Method set for givenDay opposite state of accomplish habit
     * @param calendarDay for which state is changing
     */
    public void setHabitDoneIn(CalendarDay calendarDay) {
    }

    /**
     * Probably required by Room
     */
    void setDaysOfWeek(int daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
