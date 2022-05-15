package skills.future.planer.ui.goals.creator;

import android.content.Context;
import android.view.View;

import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.ui.day.views.habits.HabitTotalAdapter;
import skills.future.planer.ui.day.views.habits.HabitViewHolder;
import skills.future.planer.ui.month.MonthFragment;

public class HabitViewHolderGoalsCreator extends HabitViewHolder {
    public HabitViewHolderGoalsCreator(View itemView, HabitViewModel habitViewModel, HabitTotalAdapter habitTotalAdapter, Context context) {
        super(itemView, habitViewModel, habitTotalAdapter, context);
    }

    @Override
    public void setCheckBoxListener(HabitData habitData, int position) {
        checkBox.setChecked(habitData.isHabitDone(MonthFragment.getGlobalSelectedDate()));
        /*checkBox.setOnClickListener(e -> {
            try {
                habitData.setHabitDoneIn(MonthFragment.getGlobalSelectedDate());
                habitViewModel.edit(habitData);
                habitTotalAdapter.notifyItemChanged(position);
            } catch (DataBaseException dataBaseException) {
                dataBaseException.printStackTrace();
            }
        });*/
    }
}
