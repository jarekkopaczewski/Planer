package skills.future.planer.ui.habit;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.ui.day.views.habits.TextAdapter;

@Getter
public class HabitExtendedViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final ProgressBar habitDayProgress;
    private final CircularProgressIndicator circularProgressIndicatorHabit;

    public HabitExtendedViewHolder(View itemView, Context context) {
        super(itemView);
        title = itemView.findViewById(R.id.habitTitleTextViewExtended);
        habitDayProgress = itemView.findViewById(R.id.habitDayProgress);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorHabit);
    }

    public void setEveryThing(String title) {
        this.title.setText(title);

        Random random = new Random();

        habitDayProgress.setMax(100);
        circularProgressIndicatorHabit.setMaxProgress(100);

        habitDayProgress.setProgress(random.nextInt(99));
        circularProgressIndicatorHabit.setCurrentProgress(random.nextInt(99));
        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());
    }
}
