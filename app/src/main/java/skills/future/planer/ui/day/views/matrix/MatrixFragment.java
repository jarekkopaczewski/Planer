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
import skills.future.planer.db.task.DayCategorizedTaskData;
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
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            taskDataViewModels.get(i).getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> {

                matrixAdapters.get(finalI).setFilteredTaskList(new DayCategorizedTaskData(taskData).getAllCategorizedDayFromQuarter(finalI));
                progressBars.get(finalI).setMax(matrixAdapters.get(finalI).getItemCount());
                progressBars.get(finalI).setProgress(matrixAdapters.get(finalI).getDone());
            });
        }
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