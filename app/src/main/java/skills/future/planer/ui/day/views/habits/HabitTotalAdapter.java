package skills.future.planer.ui.day.views.habits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.ui.AnimateView;

public class HabitTotalAdapter extends RecyclerView.Adapter<HabitViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<String> habitsList = new ArrayList<>(Arrays.asList("Picie wody rano", "Czytanie książki", "Biegnie", "Jedzenie w domu", "Nie palić"));

    public HabitTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HabitViewHolder(createViewOfItem(parent, R.layout.fragment_habit_in_list), context);
    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);

        CheckBox checkBox = itemView.findViewById(R.id.habitBoxTask);

        itemView.setOnClickListener(e->{
            AnimateView.animateInOut(itemView, context);
            checkBox.setChecked(!checkBox.isChecked());
        });
        return itemView;
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        if (habitsList != null) {
            String current = habitsList.get(position);
            holder.setEveryThing(current);
        } else // Covers the case of data not being ready yet.
            holder.getTitle().setText("No Word");
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (habitsList != null)
            return habitsList.size();
        else return 0;
    }
}
