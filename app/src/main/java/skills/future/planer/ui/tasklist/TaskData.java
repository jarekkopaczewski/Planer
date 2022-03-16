package skills.future.planer.ui.tasklist;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import skills.future.planer.R;

@Getter
@Setter
class TaskData {
    private Boolean status;
    private Drawable category;
    private Map.Entry<Priorities, Priorities> prioritiesMatrix;
    private String taskText;
    private Date startingDate = null;
    private Date endingDate = null;

    TaskData(Context context, int lifeCategory) {
        if (lifeCategory == 0)
            category = ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.home,
                    null);
        else
            category = ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.briefcase,
                    null);
    }

}
