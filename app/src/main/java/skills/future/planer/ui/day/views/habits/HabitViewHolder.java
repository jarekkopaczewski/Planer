package skills.future.planer.ui.day.views.habits;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.DataBaseException;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.ui.month.MonthFragment;

@Getter
public class HabitViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final CheckBox checkBox;
    private final HabitViewModel habitViewModel;
    private final HabitTotalAdapter habitTotalAdapter;

    public HabitViewHolder(View itemView, HabitViewModel habitViewModel, HabitTotalAdapter habitTotalAdapter) {
        super(itemView);
        this.habitViewModel = habitViewModel;
        title = itemView.findViewById(R.id.habitTitleTextView);
        checkBox = itemView.findViewById(R.id.habitBoxTask);
        this.habitTotalAdapter = habitTotalAdapter;
    }

    /**
     * Sets up all part of view with values from database
     *
     * @param habitData which store habit data
     * @param position  of given habit in lit
     */
    public void setEveryThing(HabitData habitData, int position) {
        title.setText(habitData.getTitle());
        checkBox.setChecked(habitData.isHabitDone(MonthFragment.getGlobalSelectedDate()));
        checkBox.setOnClickListener(e -> {
            try {
                habitData.setHabitDoneIn(MonthFragment.getGlobalSelectedDate());
                habitViewModel.edit(habitData);
                habitTotalAdapter.notifyItemChanged(position);
            } catch (DataBaseException dataBaseException) {
                dataBaseException.printStackTrace();
            }
        });
    }
}