package skills.future.planer.ui.habit.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.ui.goals.pager.recycler.ICustomViewHolder;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;

public class HabitViewHolder extends ICustomViewHolder {
    private final TextView title;

    public HabitViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.habitTitleTextViewExtended);
    }

    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        if (element instanceof HabitData habitData)
            this.title.setText(habitData.getTitle());
    }
}
