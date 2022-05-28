package skills.future.planer.ui.goals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentGoalsBinding;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.goals.creator.GoalsCreatorActivity;
import skills.future.planer.ui.goals.pager.GoalTotalAdapter;
import skills.future.planer.ui.habit.HabitCreatorActivity;
import skills.future.planer.ui.tasklist.TaskCreatorActivity;

public class GoalsFragment extends Fragment {
    private FragmentGoalsBinding binding;
    private GoalTotalAdapter goalTotalAdapter;
    private TextView pagerCountText;
    private Boolean othersFabVisible = false;
    private FloatingActionButton fab, fabTask, fabHabit, fabGoal;

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager2 totalGoalList = binding.totalGoalList;
        pagerCountText = binding.pagerCountText;
        pagerCountText.setText(R.string.noGoals);

        fab = binding.fab;
        fabTask = binding.fabTask;
        fabHabit = binding.fabHabit;
        fabGoal = binding.fabGoal;

        hideFABs();

        createFABListeners(totalGoalList);


        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        goalTotalAdapter = new GoalTotalAdapter(this);
        goalTotalAdapter.setFragmentManager(getChildFragmentManager());
        totalGoalList.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        totalGoalList.setAdapter(goalTotalAdapter);
        totalGoalList.setPageTransformer(new CustomTransformer());
        totalGoalList.setClipToPadding(false);
        totalGoalList.setClipChildren(false);
        totalGoalList.setClipToPadding(false);
        totalGoalList.setOverScrollMode(2);
        totalGoalList.setPadding(50, 0, 50, 0);
        totalGoalList.setOffscreenPageLimit(3);
        goalsViewModel.getAllGoals().observe(this.getViewLifecycleOwner(),
                goalData -> goalTotalAdapter.setGoalsList(goalData));

        ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (goalTotalAdapter.getItemCount() > 0)
                    pagerCountText.setText("Cel: " + (position + 1) + "/" + goalTotalAdapter.getItemCount());
                else
                    pagerCountText.setText("Cel: 0/0");
            }
        };


        totalGoalList.registerOnPageChangeCallback(onPageChangeCallback);
        return root;
    }

    /**
     * Creates listeners to FABs buttons
     */
    private void createFABListeners(ViewPager2 totalGoalList) {
        fab.setOnClickListener(view -> {
            Context context = getContext();
            if (context != null) {
                if (!othersFabVisible) {
                    fab.setForeground(ContextCompat.getDrawable(context, R.drawable.cancel));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.imageTintColor_2, null)));
                    showFABs();
                } else {
                    hideFABs();
                    fab.setForeground(ContextCompat.getDrawable(context, R.drawable.plus));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.second_color_30, null)));
                }
            }

            othersFabVisible = !othersFabVisible;
        });


        fabTask.setOnClickListener(v -> {
            var intent = new Intent(this.getContext(), TaskCreatorActivity.class);
            var bundle = new Bundle();
            bundle.putInt("goalId", totalGoalList.getCurrentItem());
            intent.putExtras(bundle);
            requireContext().startActivity(intent);
        });

        fabHabit.setOnClickListener(v -> {
            var intent = new Intent(this.getContext(), HabitCreatorActivity.class);
            var bundle = new Bundle();
            bundle.putInt("goalId", totalGoalList.getCurrentItem());
            intent.putExtras(bundle);
            requireContext().startActivity(intent);
        });

        fabGoal.setOnClickListener(v -> requireContext()
                .startActivity(new Intent(this.requireContext(), GoalsCreatorActivity.class)));
    }

    /**
     * Hides the FAB buttons associated with the add action
     */
    private void hideFABs() {
        AnimateView.singleAnimation(fabTask, getContext(), R.anim.updown);
        AnimateView.singleAnimation(fabHabit, getContext(), R.anim.updown2);
        AnimateView.singleAnimation(fabGoal, getContext(), R.anim.updown3);
        fabTask.hide();
        fabHabit.hide();
        fabGoal.hide();
    }

    /**
     * Shows the FAB buttons associated with the add action
     */
    private void showFABs() {
        fabTask.show();
        AnimateView.singleAnimation(fabTask, getContext(), R.anim.downup);
        fabHabit.show();
        AnimateView.singleAnimation(fabHabit, getContext(), R.anim.downup2);
        fabGoal.show();
        AnimateView.singleAnimation(fabGoal, getContext(), R.anim.downup3);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}