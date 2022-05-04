package skills.future.planer.ui.day.views.habits;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentHabitBinding;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.AnimateView;


public class HabitFragment extends Fragment {

    private HabitDayViewModel habitDayViewModel;
    private HabitViewModel habitViewModel;
    private FragmentHabitBinding binding;
    private HabitTotalAdapter habitTotalAdapter;
    private RecyclerView habitList;
    private CircularProgressIndicator circularProgressIndicator;

    public static HabitFragment newInstance() {
        return new HabitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        habitDayViewModel = new ViewModelProvider(this).get(HabitDayViewModel.class);
        habitViewModel = ViewModelProviders.of(this).get(HabitViewModel.class);
        habitDayViewModel.setHabitViewModel(habitViewModel);

        binding = FragmentHabitBinding.inflate(inflater, container, false);
        circularProgressIndicator = binding.circularProgressIndicator;

        habitList = binding.habitList;
        habitTotalAdapter = new HabitTotalAdapter(this.getContext(),habitViewModel);
        habitDayViewModel.setHabitTotalAdapter(habitTotalAdapter);
        habitDayViewModel.setViewLifecycleOwner(this.getViewLifecycleOwner());
        habitList.setAdapter(habitTotalAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        AnimateView.singleAnimation(binding.circularProgressIndicator, getContext(), R.anim.scalezoom2);

        circularProgressIndicator.setProgress(40.0f, 100.0f);
        circularProgressIndicator.animate();
        circularProgressIndicator.setProgressTextAdapter(new TextAdapter());

        if (circularProgressIndicator.getProgress() <= 40)
            circularProgressIndicator.setProgressColor(ContextCompat.getColor(getContext(), R.color.bad));
        else if (circularProgressIndicator.getProgress() <= 75)
            circularProgressIndicator.setProgressColor(ContextCompat.getColor(getContext(), R.color.mid));
        else
            circularProgressIndicator.setProgressColor(ContextCompat.getColor(getContext(), R.color.good));

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}