package skills.future.planer.ui.tasklist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaskListModelView extends ViewModel {

    private final MutableLiveData<String> mText;

    public TaskListModelView() {
        mText = new MutableLiveData<>();
        mText.setValue("This is task list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
