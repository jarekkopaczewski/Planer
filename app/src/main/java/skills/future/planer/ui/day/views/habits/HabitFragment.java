package skills.future.planer.ui.day.views.habits;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.databinding.FragmentHabitBinding;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;


public class HabitFragment extends Fragment {

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
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        binding = FragmentHabitBinding.inflate(inflater, container, false);
        circularProgressIndicator = binding.circularProgressIndicator;

        habitList = binding.habitList;
        habitTotalAdapter = new HabitTotalAdapter(this.getContext());
        habitList.setAdapter(habitTotalAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        circularProgressIndicator.setProgress(36.0f, 100.0f);
        circularProgressIndicator.animate();
        circularProgressIndicator.setProgressTextAdapter(new TextAdapter());

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}