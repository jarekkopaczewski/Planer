package skills.future.planer.ui.goals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentGoalsBinding;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.ui.goals.creator.GoalsCreatorActivity;
import skills.future.planer.ui.goals.pager.GoalTotalAdapter;
import skills.future.planer.ui.habit.HabitCreatorActivity;
import skills.future.planer.ui.tasklist.TaskCreatorActivity;

public class GoalsFragment extends Fragment {
    private FragmentGoalsBinding binding;
    private GoalTotalAdapter goalTotalAdapter;
    private TextView pagerCountText;

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager2 totalGoalList = binding.totalGoalList;
        pagerCountText = binding.pagerCountText;

        binding.FABMenu.setOnMenuItemClickListener(id -> {
            switch (id) {
                case R.drawable.habit_add -> this.requireContext().startActivity(new Intent(this.getContext(), HabitCreatorActivity.class));
                case R.drawable.tas_add -> this.requireContext().startActivity(new Intent(this.getContext(), TaskCreatorActivity.class));
                case R.drawable.goal_add -> this.requireContext().startActivity(new Intent(this.requireContext(), GoalsCreatorActivity.class));
            }
        });

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}