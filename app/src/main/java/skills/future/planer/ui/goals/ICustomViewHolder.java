package skills.future.planer.ui.goals;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import skills.future.planer.db.task.TaskData;

public class ICustomViewHolder extends RecyclerView.ViewHolder{
    public ICustomViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public void setEveryThing(TaskData taskData){};
    public void setEveryThing(String title){};
}
