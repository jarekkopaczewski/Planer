package skills.future.planer.ui.habit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentHabitBrowserBinding;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.ui.AnimateView;

public class HabitBrowserFragment extends Fragment {
    private HabitViewModel habitViewModel;
    private FragmentHabitBrowserBinding binding;
    private HabitExtendedTotalAdapter habitExtendedTotalAdapter;
    private RecyclerView totalHabitList;

    public static HabitBrowserFragment newInstance() {
        return new HabitBrowserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHabitBrowserBinding.inflate(inflater, container, false);
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        totalHabitList = binding.totalHabitList;
        habitExtendedTotalAdapter = new HabitExtendedTotalAdapter(this.getContext(), habitViewModel,
                this.getViewLifecycleOwner());
        totalHabitList.setAdapter(habitExtendedTotalAdapter);
        totalHabitList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitViewModel.getAllHabits().observe(this.getViewLifecycleOwner(), habitData ->
                habitExtendedTotalAdapter.setHabitsList(habitData));
        AnimateView.singleAnimation(binding.addHabitFab, getContext(), R.anim.downup);

        binding.addHabitFab.setOnClickListener(e -> startActivity(new Intent(getActivity(),
                HabitCreatorActivity.class)));


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}