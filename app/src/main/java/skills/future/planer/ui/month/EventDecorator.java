package skills.future.planer.ui.month;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

import lombok.Setter;

/**
 * Class implements DayViewDecorator.
 * Prepares decoration of dots under given dates.
 */
@Setter
public class EventDecorator implements DayViewDecorator {

    private final CustomMultipleDotSpan dots;
    /**
     * Dates which are to be decorated
     */
    private HashSet<CalendarDay> dates_tasks;

    /**
     * Constructor of EventDecorator
     *
     * @param dates       HashSet of CalendarDay - dates to be decorated
     * @param dots_number number of tasks in that day/dots
     */
    public EventDecorator(HashSet<CalendarDay> dates, int dots_number) {
        this.dates_tasks = dates;
        dots = new CustomMultipleDotSpan(5, dots_number);
    }

    /**
     * Should decorate given day
     *
     * @param day
     * @return day to be decorated
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates_tasks.contains(day);
    }

    /**
     * Decorates day. Adds dots under a date
     *
     * @param view facade of the DayView
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(dots);
    }
}
