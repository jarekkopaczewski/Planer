package skills.future.planer.ui.month;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import skills.future.planer.db.task.TaskData;

public class EventDecorator implements DayViewDecorator {

    private final int[] colors;
    private  HashSet<CalendarDay> dates;
    private HashMap<CalendarDay,Integer> dates_tasks;


    public EventDecorator(Collection<CalendarDay> dates, int[] colors) {
        this.dates = new HashSet<>(dates);
        this.colors = colors;
    }

   // public EventDecorator(HashMap<CalendarDay,Integer> dates, int[] colors) {
    //    this.dates_tasks = new HashMap<>(dates);
    //    this.colors = colors;
   // }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan((new CustomMultipleDotSpan(5,colors)));

    }


}
