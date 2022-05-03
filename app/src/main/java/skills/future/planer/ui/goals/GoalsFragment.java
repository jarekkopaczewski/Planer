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
import androidx.viewpager2.widget.ViewPager2;

import com.codeboy.pager2_transformers.Pager2_CubeInTransformer;
import com.codeboy.pager2_transformers.Pager2_CubeOutTransformer;
import com.codeboy.pager2_transformers.Pager2_DepthTransformer;
import com.codeboy.pager2_transformers.Pager2_ZoomInTransformer;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentGoalsBinding;

public class GoalsFragment extends Fragment {

    private GoalsViewModel homeViewModel;
    private FragmentGoalsBinding binding;
    private GoalTotalAdapter goalTotalAdapter;
    private ViewPager2 totalGoalList;

    public GoalsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalGoalList = binding.totalGoalList;
        goalTotalAdapter = new GoalTotalAdapter(this.getContext());
        goalTotalAdapter.setLifecycle(getLifecycle());
        goalTotalAdapter.setFragmentManager(getChildFragmentManager());

        totalGoalList.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        totalGoalList.setAdapter(goalTotalAdapter);
        totalGoalList.setOffscreenPageLimit(3);
        totalGoalList.setPageTransformer(new Pager2_ZoomInTransformer());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}