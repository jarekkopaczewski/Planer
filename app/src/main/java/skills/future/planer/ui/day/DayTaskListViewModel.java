package skills.future.planer.ui.day;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import lombok.Getter;

@Getter
public class DayTaskListViewModel extends ViewModel {

    private final MutableLiveData<CalendarDay> today = new MutableLiveData<>(CalendarDay.today());

    /**
     * Moves cursor to today's date
     */
    public void returnToToday(MaterialCalendarView materialCalendarView, FloatingActionButton fab, TextView text) {
        materialCalendarView.setCurrentDate(today.getValue(), true);
        materialCalendarView.setDateSelected(materialCalendarView.getSelectedDate(), false);
        materialCalendarView.setDateSelected(today.getValue(), true);
        changeVisibility(fab, text, View.INVISIBLE);
    }

    /**
     * Checks is needed to change visibility of fab and texView
     */
    public void checkDateIsToday(CalendarDay date, FloatingActionButton fabDay, TextView dayNumberView) {
        changeVisibility(fabDay, dayNumberView, date.equals(today.getValue()) ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * Sets visibility of fab and texView
     */
    private void changeVisibility(FloatingActionButton fab, TextView text, int visibility) {
        fab.setVisibility(visibility);
        text.setVisibility(visibility);
    }
}