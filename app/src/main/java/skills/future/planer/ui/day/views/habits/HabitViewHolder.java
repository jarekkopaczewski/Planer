package skills.future.planer.ui.day.views.habits;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;

@Getter
public class HabitViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final CheckBox checkBox;
    private final HabitViewModel habitViewModel;

    public HabitViewHolder(View itemView,HabitViewModel habitViewModel) {
        super(itemView);
        this.habitViewModel=habitViewModel;
        title = itemView.findViewById(R.id.habitTitleTextView);
        checkBox = itemView.findViewById(R.id.habitBoxTask);
    }
    //todo zrobić listenerea do zmiany czy zadanie jest zrobione czy nie
    /**
     * Sets up all part of view with values from database
     *
     * @param habitData which store habit data
     */
    public void setEveryThing(HabitData habitData) {
        this.title.setText(habitData.getTitle());
        checkBox.setOnClickListener(e ->{
            //todo habitData.setHabitDoneIn();
        });
    }
}
