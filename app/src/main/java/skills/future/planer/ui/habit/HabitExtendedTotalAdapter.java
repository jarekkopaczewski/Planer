package skills.future.planer.ui.habit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.view_holders.HabitExtendedViewHolder;

public class HabitExtendedTotalAdapter extends RecyclerView.Adapter<HabitExtendedViewHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final Fragment fragment;
    private List<HabitData> habitsList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setHabitsList(List<HabitData> habitsList) {
        this.habitsList = habitsList;
        notifyDataSetChanged();

    }

    public HabitExtendedTotalAdapter(Context context, Fragment fragment) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public HabitExtendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HabitExtendedViewHolder(createViewOfItem(parent,
                R.layout.fragment_habit_in_list_extended), context, fragment.getActivity());
    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        ImageView editButton = itemView.findViewById(R.id.editImageHabit);

        itemView.setOnClickListener(e -> AnimateView.animateInOut(itemView, context));

        // animation
        AnimateView.singleAnimation(itemView.findViewById(R.id.circularProgressIndicatorHabit), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.circularProgressIndicatorHabitDay), context, R.anim.scalezoom2);
        AnimateView.singleAnimation(itemView.findViewById(R.id.circularProgressIndicatorHabitDay), context, R.anim.scalezoom2);
        return itemView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HabitExtendedViewHolder holder, int position) {
        if (habitsList != null) {
            HabitData current = habitsList.get(position);
            holder.setEveryThing(current);
        }
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
