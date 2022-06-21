package skills.future.planer.ui.day.views.habits.viewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import skills.future.planer.db.habit.HabitData;

public abstract class ICustomHabitDayViewHolder extends RecyclerView.ViewHolder {
    public ICustomHabitDayViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract public void setEveryThing(HabitData element, int position);
}