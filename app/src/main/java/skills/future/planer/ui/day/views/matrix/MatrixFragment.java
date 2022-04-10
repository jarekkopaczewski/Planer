package skills.future.planer.ui.day.views.matrix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import skills.future.planer.databinding.FragmentMatrixBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;

public class MatrixFragment extends Fragment {

    private FragmentMatrixBinding binding;
    private MatrixModelView matrixModelView;
    private ArrayList<RecyclerView> recyclerViews;
    private ArrayList<MatrixListTotalAdapter> matrixAdapters;
    private ArrayList<TaskDataViewModel> taskDataViewModels;
    private ArrayList<ProgressBar> progressBars;

    public MatrixFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMatrixBinding.inflate(inflater, container, false);
        matrixModelView = new ViewModelProvider(this).get(MatrixModelView.class);
        View root = binding.getRoot();

        // create array lists
        matrixAdapters = new ArrayList<>();
        taskDataViewModels = new ArrayList<>();
        recyclerViews = new ArrayList<>();
        progressBars = new ArrayList<>();

        // progress bars binding
        progressBars.add(binding.urgentImportantProgressBar);
        progressBars.add(binding.urgentNotImportantProgressBar);
        progressBars.add(binding.notUrgentImportantProgressBar);
        progressBars.add(binding.notUrgentNotImportantProgressBar);

        // recycler binding
        recyclerViews.add(binding.urgentImportantRecycler);
        recyclerViews.add(binding.urgentNotImportantRecycler);
        recyclerViews.add(binding.notUrgentImportantRecycler);
        recyclerViews.add(binding.notUrgentNotImportantRecycler);

        setUpAdapters();
        setUpModels();

        return root;
    }

    /**
     * Creates and sets up adapters in loop
     * Add view
     */
    public void setUpAdapters() {
        for (int i = 0; i < 4; i++) {
            matrixAdapters.add(new MatrixListTotalAdapter(this.getContext()));
            recyclerViews.get(i).setAdapter(matrixAdapters.get(i));
            recyclerViews.get(i).setLayoutManager(new LinearLayoutManager(this.getContext()));
            taskDataViewModels.add(new ViewModelProvider(this).get(TaskDataViewModel.class));
        }
    }

    /**
     * Adds live data observers & fragmentResultsListeners
     */
    public void setUpModels() {
        // nie jest w pętli bo lambdy i trzeba zmienić getter dla każdego modelu
        taskDataViewModels.get(0).getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> {
            matrixAdapters.get(0).setFilteredTaskList(taskData);
            progressBars.get(0).setMax(matrixAdapters.get(0).getItemCount());
            progressBars.get(0).setProgress(matrixAdapters.get(0).getDone());
        });

        taskDataViewModels.get(1).getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> {
            matrixAdapters.get(1).setFilteredTaskList(taskData);
            progressBars.get(1).setMax(matrixAdapters.get(1).getItemCount());
            progressBars.get(1).setProgress(matrixAdapters.get(1).getDone());
        });

        taskDataViewModels.get(2).getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> {
            matrixAdapters.get(2).setFilteredTaskList(taskData);
            progressBars.get(2).setMax(matrixAdapters.get(2).getItemCount());
            progressBars.get(2).setProgress(matrixAdapters.get(2).getDone());
        });

        taskDataViewModels.get(3).getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> {
            matrixAdapters.get(3).setFilteredTaskList(taskData);
            progressBars.get(3).setMax(matrixAdapters.get(3).getItemCount());
            progressBars.get(3).setProgress(matrixAdapters.get(3).getDone());
        });

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            taskDataViewModels.get(0).insert(result);
        });

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            taskDataViewModels.get(1).insert(result);
        });

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            taskDataViewModels.get(2).insert(result);
        });

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            taskDataViewModels.get(3).insert(result);
        });
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