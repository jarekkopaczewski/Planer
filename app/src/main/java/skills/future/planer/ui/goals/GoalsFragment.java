package skills.future.planer.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.codeboy.pager2_transformers.Pager2_ZoomInTransformer;

import skills.future.planer.databinding.FragmentGoalsBinding;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.habit.HabitViewModel;

public class GoalsFragment extends Fragment {
    private GoalsViewModel goalsViewModel;
    private GoalsFragmentViewModel homeViewModel;
    private FragmentGoalsBinding binding;
    private GoalTotalAdapter goalTotalAdapter;
    private ViewPager2 totalGoalList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(GoalsFragmentViewModel.class);
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalGoalList = binding.totalGoalList;
        goalTotalAdapter = new GoalTotalAdapter(this.getContext(), this, new ViewModelProvider(this).get(HabitViewModel.class));
        goalTotalAdapter.setLifecycle(getLifecycle());
        goalTotalAdapter.setFragmentManager(getChildFragmentManager());
        goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        totalGoalList.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        totalGoalList.setAdapter(goalTotalAdapter);
        totalGoalList.setOffscreenPageLimit(3);
        totalGoalList.setPageTransformer(new Pager2_ZoomInTransformer());
        goalsViewModel.getAllGoals().observe(this.getViewLifecycleOwner(), goalData -> goalTotalAdapter.setGoalsList(goalData));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}