package skills.future.planer.ui.day.views.daylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import skills.future.planer.databinding.DayTaskListFragmentBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.DayFragmentDirections;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;

public class DayTaskListFragment extends Fragment {

    private DayTaskListViewModel dayTaskListViewModel;
    private TaskDataViewModel mWordViewModel;
    private DayTaskListFragmentBinding binding;
    private MaterialCalendarView materialCalendarView;
    private FloatingActionButton fabDay;
    private RecyclerView listDay;
    private TaskTotalAdapter taskTotalAdapter;
    private TextView dayNumberView;

    public DayTaskListFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DayTaskListFragmentBinding.inflate(inflater, container, false);
        dayTaskListViewModel = new ViewModelProvider(this).get(DayTaskListViewModel.class);
        View root = binding.getRoot();

        componentBindings();


        dayNumberView.setText(String.valueOf(dayTaskListViewModel.getToday().getValue().getDay()));

        fabDay.setOnClickListener(v -> dayTaskListViewModel.returnToToday(materialCalendarView, fabDay, dayNumberView));

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
                    dayTaskListViewModel.checkDateIsToday(date, fabDay, dayNumberView);
                }
        );

        dayTaskListViewModel.returnToToday(materialCalendarView, fabDay, dayNumberView);

        createList();


        return root;
    }


    //TODO Zrobić to dla widoku dnia
    private void createList() {
        listDay = binding.listTotalView;
        taskTotalAdapter = new TaskTotalAdapter(this.getContext());
        listDay.setAdapter(taskTotalAdapter);
        //listTotal.setTextFilterEnabled(true);
        //taskTotalAdapter.getFilter().filter("");
        listDay.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mWordViewModel = ViewModelProviders.of(this).get(TaskDataViewModel.class);
        mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter.setFilteredTaskList(taskData));


        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel.insert(result);
        });
        binding.fab.setOnClickListener(view -> {
            AnimateView.animateInOut(binding.fab, getContext());
            Navigation.findNavController(view).navigate(DayFragmentDirections.actionNavDayToTaskListCreatorFragment(-1));
        });

        ItemTouchHelper helper = new ItemTouchHelper(
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
                        TaskData myTaskData = taskTotalAdapter.getTaskDataAtPosition(position);
                        mWordViewModel.deleteTaskData(myTaskData);
                    }
                });

        helper.attachToRecyclerView(listDay);
    }

    /**
     * Creates bindings on components
     */
    private void componentBindings() {
        materialCalendarView = binding.calendarView;
        fabDay = binding.dayFab;
        dayNumberView = binding.dayNumber;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}