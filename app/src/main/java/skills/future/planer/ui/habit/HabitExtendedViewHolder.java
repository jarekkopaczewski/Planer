package skills.future.planer.ui.habit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.views.habits.TextAdapter;

@Getter
public class HabitExtendedViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final CircularProgressIndicator circularProgressIndicatorHabit;
    private final CircularProgressIndicator circularProgressIndicatorHabitDay;
    private final Context context;
    private final Chip monday;
    private final Chip tuesday;
    private final Chip wednesday;
    private final Chip thursday;
    private final Chip friday;
    private final Chip saturday;
    private final Chip sunday;

    public HabitExtendedViewHolder(View itemView, Context context) {
        super(itemView);
        title = itemView.findViewById(R.id.habitTitleTextViewExtended);
        circularProgressIndicatorHabit = itemView.findViewById(R.id.circularProgressIndicatorHabit);
        circularProgressIndicatorHabitDay = itemView.findViewById(R.id.circularProgressIndicatorHabitDay);
        monday = itemView.findViewById(R.id.mondayChip);
        tuesday = itemView.findViewById(R.id.tueChip);
        wednesday = itemView.findViewById(R.id.wednChip);
        thursday = itemView.findViewById(R.id.thursChip);
        friday = itemView.findViewById(R.id.fridChip);
        saturday = itemView.findViewById(R.id.saturdayChip);
        sunday = itemView.findViewById(R.id.sundayChip);
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
    public void setEveryThing(String title) {
        this.title.setText(title);

        Random random = new Random();

        circularProgressIndicatorHabit.setMaxProgress(100);
        circularProgressIndicatorHabit.setCurrentProgress(random.nextInt(99));
        circularProgressIndicatorHabit.setProgressTextAdapter(new TextAdapter());

        circularProgressIndicatorHabitDay.setMaxProgress(21);
        circularProgressIndicatorHabitDay.setCurrentProgress(random.nextInt(20));

        HabitTextAdapter habitTextAdapter = new HabitTextAdapter();
        habitTextAdapter.setMaxProgress(circularProgressIndicatorHabitDay.getMaxProgress());
        circularProgressIndicatorHabitDay.setProgressTextAdapter(habitTextAdapter);

        if (circularProgressIndicatorHabit.getProgress() <= 40)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabit.getProgress() <= 75)
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabit.setProgressColor(ContextCompat.getColor(context, R.color.good));

        if (circularProgressIndicatorHabitDay.getProgress() / circularProgressIndicatorHabitDay.getMaxProgress() <= 0.4)
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.bad));
        else if (circularProgressIndicatorHabitDay.getProgress() / circularProgressIndicatorHabitDay.getMaxProgress() <= 75)
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.mid));
        else
            circularProgressIndicatorHabitDay.setProgressColor(ContextCompat.getColor(context, R.color.good));
    }
}
