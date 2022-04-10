package skills.future.planer.ui.day.views.matrix;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MatrixModelView extends ViewModel {

    private final MutableLiveData<String> mText;

    public MatrixModelView() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MatrixModelView fragment");
    }
}
