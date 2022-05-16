package skills.future.planer.ui.day.views.habits;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.stream.Collectors;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentHabitBinding;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.HabitCreatorActivity;
import skills.future.planer.ui.month.MonthFragment;


public class HabitFragment extends Fragment {
    private HabitDayViewModel habitDayViewModel;
    private HabitViewModel habitViewModel;
    private FragmentHabitBinding binding;
    private HabitTotalAdapter habitTotalAdapter;
    private RecyclerView habitList;
    private TextView habitTextInfo;
    private CircularProgressIndicator circularProgressIndicator;
    private FloatingActionButton fabHabitDay;

    public static HabitFragment newInstance() {
        return new HabitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        habitDayViewModel = new ViewModelProvider(this).get(HabitDayViewModel.class);
        habitViewModel = ViewModelProviders.of(this).get(HabitViewModel.class);
        habitDayViewModel.setHabitViewModel(habitViewModel);

        binding = FragmentHabitBinding.inflate(inflater, container, false);

        habitList = binding.habitList;
        habitTextInfo = binding.habitTextInfo;

        fabHabitDay = binding.fabHabitDay;
        fabHabitDay.setOnClickListener(e -> startActivity(new Intent(getActivity(), HabitCreatorActivity.class)));
        AnimateView.singleAnimation(fabHabitDay, getContext(), R.anim.downup);

        habitTotalAdapter = new HabitTotalAdapter(this.getContext(), habitViewModel);
        habitDayViewModel.setHabitTotalAdapter(habitTotalAdapter);
        habitDayViewModel.setViewLifecycleOwner(this.getViewLifecycleOwner());
        habitDayViewModel.setCircularProgress(binding.circularProgressIndicator);
        habitList.setAdapter(habitTotalAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setUpProgressIndicator();

        return binding.getRoot();
    }

    private void setUpProgressIndicator() {
        circularProgressIndicator = binding.circularProgressIndicator;
        AnimateView.singleAnimation(binding.circularProgressIndicator, getContext(), R.anim.scalezoom2);

        /// set observer if sth on habitList was changed
        habitViewModel.getAllHabitDataFromDay(MonthFragment.getGlobalSelectedDate()).observe(
                this.getViewLifecycleOwner(), habitData -> {
                    Integer progressDone = 0;
                    Integer progressAll = 0;
                    for (HabitData habit : habitData.stream().filter(habits -> habits
                            .isDayOfWeekChecked(DatesParser.toLocalDate(MonthFragment.getGlobalSelectedDate())))
                            .collect(Collectors.toList())) {
                        progressDone += (habit.isHabitDone(MonthFragment.getGlobalSelectedDate()) ? 1 : 0);
                        progressAll += 1;
                    }
                    if (progressAll > 0.5)
                        circularProgressIndicator.setCurrentProgress(((double) progressDone / progressAll) * 100.0f);
                    else
                        circularProgressIndicator.setCurrentProgress(100.0f);
                });

        circularProgressIndicator.setMaxProgress(100.0f);
        circularProgressIndicator.animate();
        circularProgressIndicator.setProgressTextAdapter(new TextAdapter());

        if (circularProgressIndicator.getProgress() <= 40)
            circularProgressIndicator.setProgressColor(ContextCompat.getColor(requireContext(), R.color.bad));
        else if (circularProgressIndicator.getProgress() <= 75)
            circularProgressIndicator.setProgressColor(ContextCompat.getColor(requireContext(), R.color.mid));
        else
            circularProgressIndicator.setProgressColor(ContextCompat.getColor(requireContext(), R.color.good));
    }

    private void checkThereAreHabits() {
        if (habitTotalAdapter.getItemCount() == 0) {
            habitTextInfo.setVisibility(View.VISIBLE);
            circularProgressIndicator.setVisibility(View.INVISIBLE);
        } else {
            habitTextInfo.setVisibility(View.INVISIBLE);
            circularProgressIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //checkThereAreHabits();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}