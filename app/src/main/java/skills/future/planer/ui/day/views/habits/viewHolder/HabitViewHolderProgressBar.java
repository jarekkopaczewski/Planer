package skills.future.planer.ui.day.views.habits.viewHolder;

import android.content.Context;
import android.view.View;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.views.habits.HabitDayViewModel;
import skills.future.planer.ui.day.views.habits.TextAdapter;

public class HabitViewHolderProgressBar extends ICustomHabitDayViewHolder {
    private final Context context;
    private final HabitDayViewModel habitDayViewModel;
    private CircularProgressIndicator progressBar;

    public HabitViewHolderProgressBar(View viewOfItem, Context context, HabitDayViewModel habitDayViewModel) {
        super(viewOfItem);
//        textDescription = itemView.findViewById(R.id.summaryEditText);
        this.context = context;
        progressBar = viewOfItem.findViewById(R.id.circularProgressIndicator);
        this.habitDayViewModel = habitDayViewModel;
        setUpProgressIndicatorSettings();
    }

    @Override
    public void setEveryThing(HabitData element, int position) {

    }

    private void setUpProgressIndicatorSettings() {
        AnimateView.singleAnimation(progressBar, context, R.anim.scalezoom2);

        progressBar.setMaxProgress(100.0f);
        progressBar.animate();
        progressBar.setProgressTextAdapter(new TextAdapter());

        habitDayViewModel.setContext(context);
        habitDayViewModel.setProgressBar(progressBar);

    }
}
