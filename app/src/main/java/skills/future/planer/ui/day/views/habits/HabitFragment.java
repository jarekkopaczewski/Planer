package skills.future.planer.ui.day.views.habits;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import skills.future.planer.databinding.HabitFragmentBinding;


public class HabitFragment extends Fragment {

    private HabitViewModel habitViewModel;
    private HabitFragmentBinding binding;

    public static HabitFragment newInstance() {
        return new HabitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        binding = HabitFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}