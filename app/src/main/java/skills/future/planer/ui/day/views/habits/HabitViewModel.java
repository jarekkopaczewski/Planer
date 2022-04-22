package skills.future.planer.ui.day.views.habits;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HabitViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public HabitViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is HabitViewModel fragment");
    }
}