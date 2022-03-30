package skills.future.planer.ui.month;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonthViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MonthViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is month fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
