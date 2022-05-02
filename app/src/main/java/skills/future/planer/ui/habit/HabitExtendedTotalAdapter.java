package skills.future.planer.ui.habit;

import android.content.Context;
import android.content.Intent;
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
import skills.future.planer.ui.day.views.habits.HabitViewHolder;

public class HabitExtendedTotalAdapter extends RecyclerView.Adapter<HabitExtendedViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<String> habitsList = new ArrayList<>(Arrays.asList("Nawyk testowy", "Nawyk testowy 2", "Nawyk nawyk nawyk nawyk", "Nawyk testowy 2", "Nawyk testowy 2"));

    public HabitExtendedTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HabitExtendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HabitExtendedViewHolder(createViewOfItem(parent, R.layout.fragment_habit_in_list_extended), context);
    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);

        itemView.setOnClickListener(e->{
            AnimateView.animateInOut(itemView, context);
        });

        // animation
        AnimateView.singleAnimation(itemView.findViewById(R.id.circularProgressIndicatorHabit), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.circularProgressIndicatorHabitDay), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.circularProgressIndicatorHabitDay), context, R.anim.scalezoom2);

        AnimateView.singleAnimation(itemView.findViewById(R.id.sundayChip), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.saturdayChip), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.fridChip), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.thursChip), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.wednChip), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.tueChip), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.mondayChip), context, R.anim.scalezoom2);

        itemView.findViewById(R.id.editImageHabit).setOnClickListener(e->{
            context.startActivity(new Intent(context, HabitCreatorActivity.class));
        });

        return itemView;
    }

    @Override
    public void onBindViewHolder(@NonNull HabitExtendedViewHolder holder, int position) {
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
