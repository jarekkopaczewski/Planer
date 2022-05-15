package skills.future.planer.ui.day.views.habits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.ui.AnimateView;

public class HabitTotalAdapter extends RecyclerView.Adapter<HabitViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<HabitData> habitsList = new ArrayList<>();
    private final HabitViewModel habitViewModel;

    public HabitTotalAdapter(Context context, HabitViewModel habitViewModel) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.habitViewModel = habitViewModel;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHabitsList(List<HabitData> habitsList) {
        this.habitsList = habitsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HabitViewHolder(createViewOfItem(parent, R.layout.fragment_habit_in_day_list),
                habitViewModel, this, context);
    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        if (habitsList != null) {
            var current = habitsList.get(position);
            holder.setEveryThing(current, position);
        } else // Covers the case of data not being ready yet.
            holder.getTitle().setText("No Word");
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * Gets number of habits in list
     *
     * @return if habitList isn't null size of list
     */
    @Override
    public int getItemCount() {
        if (habitsList != null)
            return habitsList.size();
        else return 0;
    }

}
