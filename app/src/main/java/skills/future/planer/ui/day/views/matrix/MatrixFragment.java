package skills.future.planer.ui.day.views.matrix;

import static android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT;
import static android.graphics.drawable.GradientDrawable.RECTANGLE;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentMatrixBinding;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.tasklist.Colors;

public class MatrixFragment extends Fragment {

    private FragmentMatrixBinding binding;
    private MatrixModelView matrixModelView;
    private ArrayList<RecyclerView> recyclerViews;
    private ArrayList<MatrixListTotalAdapter> matrixAdapters;
    private ArrayList<TaskDataViewModel> taskDataViewModels;
    private ArrayList<ProgressBar> progressBars;
    private ArrayList<ConstraintLayout> backgroundConstrains;
    private LinearLayout linearLayoutMatrix;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMatrixBinding.inflate(inflater, container, false);
        matrixModelView = new ViewModelProvider(this).get(MatrixModelView.class);
        View root = binding.getRoot();

        linearLayoutMatrix = binding.linearLayoutMatrix;
        AnimateView.singleAnimation(binding.linearLayoutMatrix, getContext(), R.anim.scalezoom2);
        AnimateView.singleAnimation(binding.imageViewBackground, getContext(), R.anim.scalezoom2);

        // create array lists
        matrixAdapters = new ArrayList<>();
        taskDataViewModels = new ArrayList<>();
        recyclerViews = new ArrayList<>();
        progressBars = new ArrayList<>();
        backgroundConstrains = new ArrayList<>();

        // recycler binding
        recyclerViews.add(binding.urgentImportantRecycler);
        recyclerViews.add(binding.urgentNotImportantRecycler);
        recyclerViews.add(binding.notUrgentImportantRecycler);
        recyclerViews.add(binding.notUrgentNotImportantRecycler);

        // background constrains
        backgroundConstrains.add(binding.constrainUrgentImportant);
        backgroundConstrains.add(binding.constrainUrgentNotImportant);
        backgroundConstrains.add(binding.constrainNotUrgentImportant);
        backgroundConstrains.add(binding.constrainNotUrgentNotImportant);

        setUpAdapters();
        setUpBackground();

        MatrixModelView.setMatrixAdapters(matrixAdapters);
        MatrixModelView.setTaskDataViewModels(taskDataViewModels);
        MatrixModelView.setViewLifecycleOwner(this.getViewLifecycleOwner());

        return root;
    }

    /**
     * Generates new background resources for current colors
     */
    private void setUpBackground() {
        int[] colors = {Colors.getColorFromPreferences("urgentImportant", getContext()), 0x00ffffff, 0x00ffffff};
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gd.setGradientType(RADIAL_GRADIENT);
        gd.setGradientRadius(1000f);
        gd.setShape(RECTANGLE);
        gd.setGradientCenter(1.0f, 1.0f);
        gd.setAlpha(255);
        backgroundConstrains.get(0).setBackgroundDrawable(gd);

        int[] colors2 = {Colors.getColorFromPreferences("urgentNotImportant", getContext()), 0x00ffffff, 0x00ffffff};
        gd = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors2);
        gd.setGradientType(RADIAL_GRADIENT);
        gd.setGradientRadius(1000f);
        gd.setShape(RECTANGLE);
        gd.setGradientCenter(0.0f, 1.0f);
        gd.setAlpha(255);
        backgroundConstrains.get(1).setBackgroundDrawable(gd);

        int[] colors3 = {Colors.getColorFromPreferences("notUrgentImportant", getContext()), 0x00ffffff, 0x00ffffff};
        gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors3);
        gd.setGradientType(RADIAL_GRADIENT);
        gd.setGradientRadius(1000f);
        gd.setShape(RECTANGLE);
        gd.setGradientCenter(1.0f, 0.0f);
        gd.setAlpha(255);
        backgroundConstrains.get(2).setBackgroundDrawable(gd);

        int[] colors4 = {Colors.getColorFromPreferences("notUrgentNotImportant", getContext()), 0x00ffffff, 0x00ffffff};
        gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors4);
        gd.setGradientType(RADIAL_GRADIENT);
        gd.setGradientRadius(1000f);
        gd.setShape(RECTANGLE);
        gd.setGradientCenter(0.0f, 0.0f);
        gd.setAlpha(255);
        backgroundConstrains.get(3).setBackgroundDrawable(gd);
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
//            try {
//                int finalI = i;
//                taskDataViewModels.get(i).getCategorizedTaskDataFromDay(i,
//                        DatesParser.toMilliseconds(MonthFragment.getGlobalSelectedDate()))
//                        .observe(this.getViewLifecycleOwner(),
//                                taskData -> matrixAdapters.get(finalI).setFilteredTaskList(taskData));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpBackground();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}