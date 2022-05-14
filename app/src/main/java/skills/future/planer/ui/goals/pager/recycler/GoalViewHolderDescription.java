package skills.future.planer.ui.goals.pager.recycler;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import skills.future.planer.R;


public class GoalViewHolderDescription extends ICustomViewHolder {

    private final TextView textDescription;
    private final Context context;

    public GoalViewHolderDescription(View itemView, Context context, Fragment fragment) {
        super(itemView);
        textDescription = itemView.findViewById(R.id.text_slideshow2);
        this.context = context;
    }
}
