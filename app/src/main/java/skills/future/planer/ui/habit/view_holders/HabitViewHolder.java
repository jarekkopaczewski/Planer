package skills.future.planer.ui.habit.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.activity.ComponentActivity;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.ui.goals.pager.recycler.ICustomViewHolder;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;

public class HabitViewHolder extends ICustomViewHolder {
    protected final TextView title;

    public HabitViewHolder(View itemView, Context context, ComponentActivity activity) {
        super(itemView);
        title = itemView.findViewById(R.id.habitTitleTextViewExtended);
    }

    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        if (element instanceof HabitData habitData)
            this.title.setText(habitData.getTitle());
    }
}
