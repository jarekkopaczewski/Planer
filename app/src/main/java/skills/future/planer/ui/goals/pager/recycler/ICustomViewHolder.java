package skills.future.planer.ui.goals.pager.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ICustomViewHolder extends RecyclerView.ViewHolder {
    public ICustomViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract public void setEveryThing(MixedRecyclerElement element) throws Exception;
}
