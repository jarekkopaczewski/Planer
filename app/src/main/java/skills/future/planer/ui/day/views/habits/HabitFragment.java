package skills.future.planer.ui.day.views.habits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentHabitBinding;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.HabitCreatorActivity;


public class HabitFragment extends Fragment {
    private HabitDayViewModel habitDayViewModel;
    private HabitViewModel habitViewModel;
    private FragmentHabitBinding binding;
    private HabitTotalAdapter habitTotalAdapter;
    private RecyclerView habitList;
    private CircularProgressIndicator progressBar;
    private FloatingActionButton fabHabitDay;
    private Context context;

    public static HabitFragment newInstance() {
        return new HabitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        habitDayViewModel = new ViewModelProvider(this).get(HabitDayViewModel.class);
        habitViewModel = ViewModelProviders.of(this).get(HabitViewModel.class);
        habitDayViewModel.setHabitViewModel(habitViewModel);
        context = requireContext();
        binding = FragmentHabitBinding.inflate(inflater, container, false);

        progressBar = binding.circularProgressIndicator;
        habitList = binding.habitList;
        fabHabitDay = binding.fabHabitDay;
        fabHabitDay.setOnClickListener(e -> startActivity(new Intent(getActivity(), HabitCreatorActivity.class)));
        AnimateView.singleAnimation(fabHabitDay, getContext(), R.anim.downup);

        habitTotalAdapter = new HabitTotalAdapter(this.getContext(), habitViewModel);
        habitDayViewModel.setHabitTotalAdapter(habitTotalAdapter);
        habitDayViewModel.setViewLifecycleOwner(this.getViewLifecycleOwner());
        habitList.setAdapter(habitTotalAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setUpProgressIndicatorSettings();

        return binding.getRoot();
    }

    private void setUpProgressIndicatorSettings() {
        AnimateView.singleAnimation(progressBar, getContext(), R.anim.scalezoom2);

        progressBar.setMaxProgress(100.0f);
        progressBar.animate();
        progressBar.setProgressTextAdapter(new TextAdapter());

        habitDayViewModel.setContext(context);
        habitDayViewModel.setProgressBar(progressBar);

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