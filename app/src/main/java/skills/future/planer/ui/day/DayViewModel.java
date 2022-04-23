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
import skills.future.planer.ui.day.views.daylist.DayTaskListViewModel;
import skills.future.planer.ui.day.views.matrix.MatrixModelView;

@Getter
public class DayViewModel extends ViewModel {

    private final MutableLiveData<CalendarDay> today = new MutableLiveData<>(CalendarDay.today());

    /**
     * Moves cursor to today's date
     */
    public void returnToToday(@NonNull MaterialCalendarView materialCalendarView,
                              @NonNull FloatingActionButton fab,
                              @NonNull TextView text) {
        materialCalendarView.setCurrentDate(today.getValue(), true);
        materialCalendarView.setDateSelected(materialCalendarView.getSelectedDate(), false);
        materialCalendarView.setDateSelected(today.getValue(), true);
        changeVisibility(fab, text, View.INVISIBLE);
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
    public void checkPagerChange(int position,
                                 ViewPager viewPager,
                                 CalendarDay date,
                                 FloatingActionButton fabDay,
                                 TextView dayNumberView,
                                 MatrixModelView matrixModelView,
                                 DayTaskListViewModel dayTaskListViewModel) {
        if (viewPager.getAdapter().getPageTitle(position).equals("Lista zadań")) {
            updateTaskList(date, dayTaskListViewModel);
            checkDateIsToday(date, fabDay, dayNumberView);
        } else
            changeVisibility(fabDay, dayNumberView, View.INVISIBLE);

        if (viewPager.getAdapter().getPageTitle(position).equals("Macierz"))
            updateMatrix(date, matrixModelView);
    }

    private void updateMatrix(CalendarDay date, MatrixModelView matrixModelView) {
        matrixModelView.setUpModels(date);
    }

    private void updateTaskList(CalendarDay date, DayTaskListViewModel dayTaskListViewModel) {
        dayTaskListViewModel.updateDate(date);
    }
}


