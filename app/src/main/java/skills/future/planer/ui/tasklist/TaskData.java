package skills.future.planer.ui.tasklist;

import lombok.Getter;
import lombok.Setter;
import android.graphics.drawable.Icon;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
class TaskData {
   private Icon category;
   private Map.Entry<Priorities, Priorities> prioritiesMatrix;
   private String taskText;
   private Date startingDate;
   private Date endingDate;
}
