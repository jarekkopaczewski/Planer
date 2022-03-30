package skills.future.planer.ui.tasklist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaskCreatorModelView extends ViewModel {

    private final MutableLiveData<String> mText;

    public TaskCreatorModelView() {
        mText = new MutableLiveData<>();
        mText.setValue("This is task creator fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
