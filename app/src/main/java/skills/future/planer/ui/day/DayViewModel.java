package skills.future.planer.ui.day;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import lombok.Getter;

@Getter
public class DayViewModel extends ViewModel {

    /**
     * Moves cursor to  date
     */
    public void returnToDate(@NonNull MaterialCalendarView materialCalendarView,
                             CalendarDay date) {
        materialCalendarView.setCurrentDate(date, true);
        materialCalendarView.setDateSelected(materialCalendarView.getSelectedDate(), false);
        materialCalendarView.setDateSelected(date, true);
    }

    /**
     * Return true if current pager position is on task list view
     */
    public boolean checkIsTaskListView(@NonNull ViewPager viewPager) {
        return viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()).equals("Lista zadań");
    }

    /**
     * Return true if current pager position is on matrix view
     */
    public boolean checkIsMatrixView(@NonNull ViewPager vpPager) {
        return vpPager.getAdapter().getPageTitle(vpPager.getCurrentItem()).equals("Macierz");
    }

    /**
     * Return true if current pager position is on habit view
     */
    public boolean checkIsHabitsView(@NonNull ViewPager vpPager) {
        return vpPager.getAdapter().getPageTitle(vpPager.getCurrentItem()).equals("Nawyki");
    }
}


