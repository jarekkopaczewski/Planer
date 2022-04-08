package skills.future.planer.ui.day.views.matrix;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentMatrixBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.tasklist.TaskListFragmentDirections;

public class MatrixFragment extends Fragment {

    private FragmentMatrixBinding binding;
    private MatrixModelView matrixModelView;
    private RecyclerView listTotal;
    private RecyclerView listTotal2;
    private RecyclerView listTotal3;
    private RecyclerView listTotal4;
    private MatrixTotalAdapter taskTotalAdapter;
    private MatrixTotalAdapter taskTotalAdapter2;
    private MatrixTotalAdapter taskTotalAdapter3;
    private MatrixTotalAdapter taskTotalAdapter4;
    private TaskDataViewModel mWordViewModel;
    private TaskDataViewModel mWordViewModel2;
    private TaskDataViewModel mWordViewModel3;
    private TaskDataViewModel mWordViewModel4;

    public MatrixFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMatrixBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        taskTotalAdapter = new MatrixTotalAdapter(this.getContext(), Priorities.Important, TimePriority.Urgent);
        taskTotalAdapter2 = new MatrixTotalAdapter(this.getContext(), Priorities.Important, TimePriority.NotUrgent);
        taskTotalAdapter3 = new MatrixTotalAdapter(this.getContext(), Priorities.NotImportant, TimePriority.Urgent);
        taskTotalAdapter4 = new MatrixTotalAdapter(this.getContext(), Priorities.NotImportant, TimePriority.NotUrgent);

        listTotal = binding.testList;
        listTotal2 = binding.testList2;
        listTotal3 = binding.testList3;
        listTotal4 = binding.testList4;

        listTotal.setAdapter(taskTotalAdapter);
        listTotal.setLayoutManager(new LinearLayoutManager(this.getContext()));

        listTotal2.setAdapter(taskTotalAdapter);
        listTotal2.setLayoutManager(new LinearLayoutManager(this.getContext()));

        listTotal3.setAdapter(taskTotalAdapter);
        listTotal3.setLayoutManager(new LinearLayoutManager(this.getContext()));

        listTotal4.setAdapter(taskTotalAdapter);
        listTotal4.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mWordViewModel = new ViewModelProvider(this).get(TaskDataViewModel.class);
        mWordViewModel2 = new ViewModelProvider(this).get(TaskDataViewModel.class);
        mWordViewModel3 = new ViewModelProvider(this).get(TaskDataViewModel.class);
        mWordViewModel4 = new ViewModelProvider(this).get(TaskDataViewModel.class);

        mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter.setFilteredTaskList(taskData));
        mWordViewModel2.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter2.setFilteredTaskList(taskData));
        mWordViewModel3.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter2.setFilteredTaskList(taskData));
        mWordViewModel4.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter2.setFilteredTaskList(taskData));


        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel.insert(result);
        });

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel2.insert(result);
        });

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel3.insert(result);
        });

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel4.insert(result);
        });

        taskTotalAdapter.getFilter().filter(" ");
        taskTotalAdapter2.getFilter().filter(" ");
        taskTotalAdapter3.getFilter().filter(" ");
        taskTotalAdapter4.getFilter().filter(" ");

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}