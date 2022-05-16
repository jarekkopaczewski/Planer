package skills.future.planer.ui.habit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
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
                R.layout.fragment_habit_in_list_extended), context, fragment);
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

        editButton.setOnClickListener(e -> {
            AnimateView.animateInOut(editButton, context);
            context.startActivity(new Intent(context, HabitCreatorActivity.class));
        });


        return itemView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HabitExtendedViewHolder holder, int position) {
        if (habitsList != null) {
            HabitData current = habitsList.get(position);
            holder.setEveryThing(current);
        } else // Covers the case of data not being ready yet.
//            holder.getTitle().setText("No Word");

        createListenerToEditButton(holder, position);
        createListenerToTrashButton(holder, position);
    }

    private void createListenerToEditButton(@NonNull HabitExtendedViewHolder holder, int position) {
        holder.itemView.findViewById(R.id.editImageHabit).setOnClickListener(e -> {
            var intent = new Intent(fragment.getActivity(), HabitCreatorActivity.class);
            var bundle = new Bundle();
            bundle.putLong("habitToEditId", habitsList.get(position).getHabitId());
            intent.putExtras(bundle);
            fragment.requireActivity().startActivity(intent);
            notifyItemChanged(position);
        });
    }

    private void createListenerToTrashButton(@NonNull HabitExtendedViewHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.confirm_deletion);
        builder.setMessage(R.string.confirm_deletion_2);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.removetask);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    new ViewModelProvider(fragment).get(HabitViewModel.class).delete(habitsList.get(position));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            holder.itemView.startAnimation(animation);
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        holder.itemView.findViewById(R.id.trashImageViewHabit).setOnClickListener(e -> alert.show());
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
