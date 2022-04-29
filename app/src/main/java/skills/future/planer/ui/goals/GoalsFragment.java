package skills.future.planer.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import skills.future.planer.databinding.FragmentGoalsBinding;

public class GoalsFragment extends Fragment {

    private GoalsViewModel homeViewModel;
    private FragmentGoalsBinding binding;
    private GoalTotalAdapter goalTotalAdapter;
    private RecyclerView totalGoalList;

    public GoalsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalGoalList = binding.totalGoalList;
        goalTotalAdapter = new GoalTotalAdapter(this.getContext());
        totalGoalList.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        totalGoalList.setAdapter(goalTotalAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}