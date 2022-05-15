package skills.future.planer.ui.goals.pager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.goals.creator.GoalsCreatorActivity;

@Setter
public class GoalTotalAdapter extends RecyclerView.Adapter<GoalViewHolder> {
    private final LayoutInflater layoutInflater;
    private final Fragment fragment;
    private final Context context;
    private FragmentManager fragmentManager;
    private List<GoalData> goalsList = new ArrayList<>();


    public GoalTotalAdapter(Fragment fragment) {
        this.layoutInflater = LayoutInflater.from(fragment.getContext());
        this.fragment = fragment;
        context = fragment.getContext();
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoalViewHolder(createViewOfItem(parent, R.layout.goal_in_list), fragment);
    }

    private void createListenerToEditButton(@NonNull GoalViewHolder holder, int position) {
        holder.itemView.findViewById(R.id.editImageBtn).setOnClickListener(e -> {
            var intent = new Intent(context, GoalsCreatorActivity.class);
            var bundle = new Bundle();
            bundle.putLong("goalIdToEdit", goalsList.get(position).getGoalId());
            intent.putExtras(bundle);
            context.startActivity(intent);
            notifyItemChanged(position);
        });
    }

    private void createListenerToTrashButton(@NonNull GoalViewHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm deletion");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            new ViewModelProvider(fragment).get(GoalsViewModel.class).delete(goalsList.get(position));
            dialog.dismiss();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        holder.itemView.findViewById(R.id.trashImageView).setOnClickListener(e -> alert.show());
    }

    @SuppressLint("NonConstantResourceId")
    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        if (goalsList != null) {
            GoalData current = goalsList.get(position);
            holder.setEveryThing(current);

        }  // Covers the case of data not being ready yet.
        //holder.getTitle().setText("No Word");

        /*createListenerToEditButton(holder, position);
        createListenerToTrashButton(holder, position);*/
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (goalsList != null)
            return goalsList.size();
        else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setGoalsList(List<GoalData> goalsList) {
        this.goalsList = goalsList;
        notifyDataSetChanged();
    }
}
