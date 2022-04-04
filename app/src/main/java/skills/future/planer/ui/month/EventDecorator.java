package skills.future.planer.ui.month;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import java.util.HashSet;

/**
 * Class implements DayViewDecorator.
 * Prepares decoration of dots under given dates.
 */
public class EventDecorator implements DayViewDecorator {

    /**
     * Dates which are to be decorated
     */
    private HashSet<CalendarDay> dates_tasks;
    /**
     * Numer of tasks in given day represented by dots
     */
    private int taskNumber;

    /**
     * Constructor of EventDecorator
     * @param dates HashSet of CalendarDay - dates to be decorated
     * @param taskNumber number of tasks in that day/dots
     */
    public EventDecorator(HashSet<CalendarDay> dates, int taskNumber) {
        this.dates_tasks = new HashSet<>(dates);
        this.taskNumber = taskNumber;
    }

    /**
     * Should decorate given day
     * @param day
     * @return day to be decorated
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates_tasks.contains(day);
    }

    /**
     * Decorates day. Adds dots under a date
     * @param view facade of the DayView
     */
    @Override
    public void decorate(DayViewFacade view) {

        //adding dots (radius of the dot, number of dots)
        view.addSpan((new CustomMultipleDotSpan(5, taskNumber)));
    }
}
