package skills.future.planer.ui.day.views.daylist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import skills.future.planer.R;
import skills.future.planer.databinding.DayTaskListFragmentBinding;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.DayFragmentDirections;
import skills.future.planer.ui.tasklist.AddTaskActivity;

public class DayTaskListFragment extends Fragment {

    private DayTaskListViewModel dayListViewModel;
    private TaskDataViewModel mTaskViewModel;
    private DayTaskListFragmentBinding binding;
    private TaskDayAdapter taskDayAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DayTaskListFragmentBinding.inflate(inflater, container, false);
        dayListViewModel = new ViewModelProvider(this).get(DayTaskListViewModel.class);
        mTaskViewModel = ViewModelProviders.of(this).get(TaskDataViewModel.class);

        View root = binding.getRoot();

        createList();

        AnimateView.singleAnimation(binding.fab, getContext(), R.anim.downup);

        dayListViewModel.setTaskViewModel(mTaskViewModel);
        dayListViewModel.setTaskDayAdapter(taskDayAdapter);
        dayListViewModel.setLifecycleOwner(this.getViewLifecycleOwner());
        return root;
    }


    /**
     * Creates list of tasks
     */
    private void createList() {
        RecyclerView listDay = binding.listTotalView;
        taskDayAdapter = new TaskDayAdapter(this.getContext(), getActivity());
        listDay.setAdapter(taskDayAdapter);
        listDay.setLayoutManager(new LinearLayoutManager(this.getContext()));

        createListenerForFab();
    }

    /**
     * Creates listener for new task creator fab button
     */
    private void createListenerForFab() {
        binding.fab.setOnClickListener(view -> {
            AnimateView.animateInOut(binding.fab, getContext());
            this.requireContext().startActivity(new Intent(this.getContext(), AddTaskActivity.class));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        taskDayAdapter.notifyDataSetChanged();
    }
}