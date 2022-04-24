package skills.future.planer.ui.day.views.daylist;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import skills.future.planer.databinding.DayTaskListFragmentBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.DayFragmentDirections;

public class DayTaskListFragment extends Fragment {

    private DayTaskListViewModel dayListViewModel;
    private TaskDataViewModel mWordViewModel;
    private DayTaskListFragmentBinding binding;
    private TaskDayAdapter taskDayAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DayTaskListFragmentBinding.inflate(inflater, container, false);
        dayListViewModel = new ViewModelProvider(this).get(DayTaskListViewModel.class);
        mWordViewModel = ViewModelProviders.of(this).get(TaskDataViewModel.class);

        View root = binding.getRoot();

        createList();

        dayListViewModel.setWordViewModel(mWordViewModel);
        dayListViewModel.setTaskDayAdapter(taskDayAdapter);
        dayListViewModel.setLifecycleOwner(this.getViewLifecycleOwner());

        return root;
    }


    /**
     * Creates list of tasks
     */
    private void createList() {
        RecyclerView listDay = binding.listTotalView;
        taskDayAdapter = new TaskDayAdapter(this.getContext());
        listDay.setAdapter(taskDayAdapter);
        listDay.setLayoutManager(new LinearLayoutManager(this.getContext()));

        createListenerForFab();
        createItemTouchHelper(listDay);
    }

    /**
     * Creates listener for new task creator fab button
     */
    private void createListenerForFab() {
        binding.fab.setOnClickListener(view -> {
            AnimateView.animateInOut(binding.fab, getContext());
            Navigation.findNavController(view)
                    .navigate(DayFragmentDirections.actionNavDayToTaskListCreatorFragment(-1));

        });
    }

    /**
     * Creates item touch helper
     */
    private void createItemTouchHelper(RecyclerView listDay) {
        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        TaskData myTaskData = taskDayAdapter.getTaskDataAtPosition(position);
                        mWordViewModel.deleteTaskData(myTaskData);
                    }
                }).attachToRecyclerView(listDay);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}