package skills.future.planer.ui.day;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import skills.future.planer.databinding.DayTaskListFragmentBinding;

public class DayTaskListFragment extends Fragment {

    private DayTaskListViewModel dayTaskListViewModel;
    private DayTaskListFragmentBinding binding;

    public static DayTaskListFragment newInstance() {
        return new DayTaskListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dayTaskListViewModel = new ViewModelProvider(this).get(DayTaskListViewModel.class);
        binding = DayTaskListFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}