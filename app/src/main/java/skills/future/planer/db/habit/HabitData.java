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
    private Integer habitId;
    private String title;
    /**
     * 7 bits, first bit is monday, last sunday, 1-habit is doing in this day, 0 opposite
     */
    private Integer daysOfWeek;
    /**
     * days number
     */
    private HabitDuration habitDuration;
    private Long beginDay;
    @Getter(AccessLevel.NONE)
    private Long dayCheckingPt1, dayCheckingPt2;

    public HabitData() {
        habitId = 0;
        title= "";
        daysOfWeek =0;
        habitDuration = HabitDuration.Short;
        beginDay = 0L;
        dayCheckingPt1=dayCheckingPt2=0L ;
    }

    public HabitData(String title, String daysOfWeek, HabitDuration habitDuration, long beginDay,
                     long dayCheckingPt1, long dayCheckingPt2) {
        this.habitId = 0;
        this.title = title;
        this.daysOfWeek = Integer.parseInt(daysOfWeek);
        this.habitDuration = habitDuration;
        this.beginDay = beginDay;
        this.dayCheckingPt1 = dayCheckingPt1;
        this.dayCheckingPt2 = dayCheckingPt2;
    }

    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = Integer.parseInt(daysOfWeek);
    }

    /**
     * Probably required by Room
     */
    void setDaysOfWeek(int daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * @return list of calendar days where habit was done
     */
    public List<CalendarDay> getAllDaysWhereHabitsWasDone(){
        return null;
    }

    /**
     * @return list of calendar days where habit was done
     */
    public int getNumberOfDaysWhereHabitsWasDone(){
        return 0;
    }

    public void setHabitDoneIn(CalendarDay calendarDay){
    }
}
