package skills.future.planer.ui.day.views.habits;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import skills.future.planer.R;

@Getter
public class HabitViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final CheckBox checkBox;


    public HabitViewHolder(View itemView, Context context) {
        super(itemView);
        title = itemView.findViewById(R.id.habitTitleTextView);
        checkBox = itemView.findViewById(R.id.habitBoxTask);
    }

    public void setEveryThing(String title) {
        this.title.setText(title);
    }
}
