package skills.future.planer.ui.habit.view_holders;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.goals.pager.recycler.MixedRecyclerElement;
import skills.future.planer.ui.habit.HabitCreatorActivity;

@Getter
public class HabitExtendedViewHolder extends HabitViewHolder {
    private final Context context;
    private final ChipGroup chipGroup;
    private final Chip goalChip;
    private final ComponentActivity activity;
    private final TextView goalLabel;
    private final View constraintLayout;
    private final ImageView imageEdit;
    private final ImageView imageTrash;


    public HabitExtendedViewHolder(View itemView, Context context, ComponentActivity activity) {
        super(itemView, context, activity);
        chipGroup = itemView.findViewById(R.id.chipGroupWeek);
        this.context = context;
        this.activity = activity;
        goalChip = itemView.findViewById(R.id.goalChip);
        goalLabel = itemView.findViewById(R.id.goalText);
        constraintLayout = itemView.findViewById(R.id.constraintLayout);
        this.imageEdit = itemView.findViewById(R.id.editImageHabit);
        this.imageTrash = itemView.findViewById(R.id.trashImageViewHabit);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void setEveryThing(MixedRecyclerElement element) {
        super.setEveryThing(element);
        if (element instanceof HabitData habitData) {
            setUpChipGroup(habitData);
            setUpGoalChip(habitData);
            createListenerToEditButton(habitData);
            createListenerToTrashButton(habitData);
        }
    }

    private void setUpChipGroup(HabitData habitData) {
        chipGroup.removeAllViews();
        var days = habitData.getDaysOfWeek();
        for (int i = 0; i < days.length(); i++) {
            if (days.charAt(i) == '1') {
                var chip = new Chip(context);
                chip.setText(habitData.getDayWeekName(i));
                chip.setCheckable(false);
                chipGroup.addView(chip);
            }
        }
    }

    private void setUpGoalChip(HabitData habitData) {
        GoalsViewModel goalsViewModel = new ViewModelProvider(activity).get(GoalsViewModel.class);
        GoalData goal = goalsViewModel.findById(habitData.getForeignKeyToGoal());
        if (goal != null) {
            goalChip.setVisibility(View.VISIBLE);
            goalLabel.setVisibility(View.VISIBLE);
            constraintLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 300, activity.getResources().getDisplayMetrics());
            String goalText = goal.getTitle();
            if (goalText.length() > 30) {
                goalText = goalText.substring(0, 27) + "...";
            }
            goalChip.setText(goalText);
        } else {
            goalChip.setVisibility(View.INVISIBLE);
            goalLabel.setVisibility(View.INVISIBLE);
            constraintLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 220, activity.getResources().getDisplayMetrics());
        }
    }

    private void createListenerToEditButton(HabitData habitData) {
        imageEdit.setOnClickListener(e -> {
            var intent = new Intent(activity, HabitCreatorActivity.class);
            var bundle = new Bundle();
            bundle.putLong("habitToEditId", habitData.getHabitId());
            intent.putExtras(bundle);
            activity.startActivity(intent);
            //notifyItemChanged(position);
        });
    }

    private void createListenerToTrashButton(HabitData habitData) {
        imageTrash.setOnClickListener(e -> {
            new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded)
                    .setIcon(R.drawable.warning)
                    .setTitle(R.string.confirm_deletion)
                    .setMessage(R.string.confirm_deletion_2)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.removetask);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {}
                            @Override
                            public void onAnimationEnd(Animation animation) { new ViewModelProvider(activity).get(HabitViewModel.class).delete(habitData); }
                            @Override
                            public void onAnimationRepeat(Animation animation) {}
                        });
                        this.itemView.startAnimation(animation);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}
