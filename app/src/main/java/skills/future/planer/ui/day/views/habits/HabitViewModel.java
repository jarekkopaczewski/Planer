package skills.future.planer.ui.day.views.habits;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class HabitViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public HabitViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is HabitViewModel fragment");
    }

    // TODO uzupełnić pobierania nawyków z bazy
    public void setUpModels(CalendarDay selectedDay) {
    }
}