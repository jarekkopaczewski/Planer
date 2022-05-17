package skills.future.planer.ui.goals.pager.recycler;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.activity.ComponentActivity;

import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;


public class GoalViewHolderDescription extends ICustomViewHolder {

    private final TextView textDescription;
    private final Context context;

    public GoalViewHolderDescription(View itemView, Context context, ComponentActivity activity) {
        super(itemView);
        textDescription = itemView.findViewById(R.id.text_slideshow2);
        this.context = context;
    }

    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        textDescription.setText(((GoalData) element).getDetails());
    }
}
