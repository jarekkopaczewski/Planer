package skills.future.planer.ui.day.views.habits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private CircularProgressIndicator circularProgressIndicator;
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
        habitDayViewModel.setContext(context);
        binding = FragmentHabitBinding.inflate(inflater, container, false);

        circularProgressIndicator = binding.circularProgressIndicator;
        habitList = binding.habitList;
        fabHabitDay = binding.fabHabitDay;
        fabHabitDay.setOnClickListener(e -> startActivity(new Intent(getActivity(), HabitCreatorActivity.class)));
        AnimateView.singleAnimation(fabHabitDay, getContext(), R.anim.downup);

        habitTotalAdapter = new HabitTotalAdapter(this.getContext(), habitViewModel);
        habitDayViewModel.setHabitTotalAdapter(habitTotalAdapter);
        habitDayViewModel.setViewLifecycleOwner(this.getViewLifecycleOwner());
        habitList.setAdapter(habitTotalAdapter);
        habitList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setUpProgressIndicator();
        habitDayViewModel.setCircularProgress(circularProgressIndicator);

        return binding.getRoot();
    }

    private void setUpProgressIndicator() {
        AnimateView.singleAnimation(circularProgressIndicator, getContext(), R.anim.scalezoom2);

        /// set observer if sth on habitList was changed
//        habitViewModel.getAllHabitDataFromDay(MonthFragment.getGlobalSelectedDate()).observe(
//                this.getViewLifecycleOwner(), habitData -> {
//                    Integer progressDone = 0;
//                    Integer progressAll = 0;
//                    for (HabitData habit : habitData.stream().filter(habits -> habits
//                            .isDayOfWeekChecked(DatesParser.toLocalDate(MonthFragment.getGlobalSelectedDate())))
//                            .collect(Collectors.toList())) {
//                        progressDone += (habit.isHabitDone(MonthFragment.getGlobalSelectedDate()) ? 1 : 0);
//                        progressAll += 1;
//                    }
//                    if (progressAll > 0.5)
//                    {
//                        circularProgressIndicator.setCurrentProgress(((double) progressDone / progressAll) * 100.0f);
//                        System.out.println(circularProgressIndicator.getProgress());
//                    }
//                    else
//                        circularProgressIndicator.setCurrentProgress(100.0f);
//
//                    updateColor();
//                });

        circularProgressIndicator.setMaxProgress(100.0f);
        circularProgressIndicator.animate();
        circularProgressIndicator.setProgressTextAdapter(new TextAdapter());

        System.out.println(circularProgressIndicator);

        circularProgressIndicator.setOnProgressChangeListener((e, f)->{
            if (e <= 40.0f) {
                circularProgressIndicator.setProgressColor(ContextCompat.getColor(context, R.color.bad));
            } else if (e <= 75.0f) {
                circularProgressIndicator.setProgressColor(ContextCompat.getColor(context, R.color.mid));
            } else {
                circularProgressIndicator.setProgressColor(ContextCompat.getColor(context, R.color.good));
            }
        });
    }

//    public void updateColor()
//    {
//        System.out.println("Fragment observwer");
//        System.out.println(circularProgressIndicator.getProgress());
//
//        if (circularProgressIndicator.getProgress() <= 40.0f)
//        {
//            circularProgressIndicator.setProgressColor(ContextCompat.getColor(context, R.color.bad));
//            System.out.println(ContextCompat.getColor(context, R.color.bad));
//        }
//        else if (circularProgressIndicator.getProgress() <= 75.0f)
//        {
//            circularProgressIndicator.setProgressColor(ContextCompat.getColor(context, R.color.mid));
//            System.out.println(ContextCompat.getColor(context, R.color.mid));
//        }
//        else
//        {
//            circularProgressIndicator.setProgressColor(ContextCompat.getColor(context, R.color.good));
//            System.out.println(ContextCompat.getColor(context, R.color.good));
//        }
//    }

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