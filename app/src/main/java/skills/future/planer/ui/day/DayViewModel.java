package skills.future.planer.ui.day;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import lombok.Getter;

@Getter
public class DayViewModel extends ViewModel {

    private final MutableLiveData<CalendarDay> today = new MutableLiveData<>(CalendarDay.today());
    private static final MutableLiveData<MaterialCalendarView> refToCalendar = new MutableLiveData<>();
    /**
     * Moves cursor to today's date
     */
    public void returnToToday(@NonNull MaterialCalendarView materialCalendarView, @NonNull FloatingActionButton fab, @NonNull TextView text) {
        materialCalendarView.setCurrentDate(today.getValue(), true);
        materialCalendarView.setDateSelected(materialCalendarView.getSelectedDate(), false);
        materialCalendarView.setDateSelected(today.getValue(), true);
        changeVisibility(fab, text, View.INVISIBLE);
    }

    void setRefToCalendar(MaterialCalendarView calendarView){
        refToCalendar.setValue(calendarView);
    }

    public MutableLiveData<MaterialCalendarView> getRefToCalendar() {
        return refToCalendar;
    }

    /**
     * Return true if current pager position is on task list view
     */
    public boolean checkIsTaskListView(@NonNull ViewPager viewPager) {
        return viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()).equals("Lista zadań");
    }

    /**
     * Checks is needed to change visibility of fab and texView
     */
    public void checkDateIsToday(@NonNull CalendarDay date, FloatingActionButton fabDay, TextView dayNumberView) {
        changeVisibility(fabDay, dayNumberView, date.equals(today.getValue()) ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * Sets visibility of fab and texView
     */
    public void changeVisibility(@NonNull FloatingActionButton fab, @NonNull TextView text, int visibility) {
        fab.setVisibility(visibility);
        text.setVisibility(visibility);
    }

    /**
     * Checks is needed to change visibility of fab when page is changed
     */
    public void checkPagerChange(int position, ViewPager viewPager, CalendarDay date, FloatingActionButton fabDay, TextView dayNumberView) {
        if (viewPager.getAdapter().getPageTitle(position).equals("Lista zadań"))
            changeVisibility(fabDay, dayNumberView, View.VISIBLE);
        else
            checkDateIsToday(date, fabDay, dayNumberView);
    }
}


