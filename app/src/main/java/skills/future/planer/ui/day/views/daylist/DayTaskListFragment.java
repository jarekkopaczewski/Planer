package skills.future.planer.ui.day.views.daylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

import skills.future.planer.databinding.DayTaskListFragmentBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.DayFragmentDirections;
import skills.future.planer.ui.day.DayViewModel;
import skills.future.planer.ui.tasklist.TaskTotalAdapter;

public class DayTaskListFragment extends Fragment {

    private DayTaskListViewModel dayListViewModel;
    private TaskDataViewModel mWordViewModel;
    private DayViewModel dayViewModel;
    private DayTaskListFragmentBinding binding;
    private RecyclerView listDay;
    private TaskTotalAdapter taskDayAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DayTaskListFragmentBinding.inflate(inflater, container, false);
        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);
        dayListViewModel = new ViewModelProvider(this).get(DayTaskListViewModel.class);
        View root = binding.getRoot();
        createList();

        return root;
    }

    //TODO Zrobić to dla widoku dnia
    private void createList() {
        listDay = binding.listTotalView;
        taskDayAdapter = new TaskTotalAdapter(this.getContext());
        listDay.setAdapter(taskDayAdapter);
        listDay.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mWordViewModel = ViewModelProviders.of(this).get(TaskDataViewModel.class);

        //dayListViewModel.setWordViewModel(mWordViewModel);
        // dayListViewModel.setTaskDayAdapter(taskDayAdapter);
        //dayListViewModel.setLifecycleOwner(this.getViewLifecycleOwner());

        var calendar = DayViewModel.getRefToCalendar().getValue();

        if (calendar != null) {
            updateList(calendar);
            updateDate(calendar.getSelectedDate());
        }


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
                        TaskData myTaskData = taskDayAdapter.getTaskDataAtPosition(position);
                        mWordViewModel.deleteTaskData(myTaskData);
                    }
                });

        helper.attachToRecyclerView(listDay);
    }

    private void updateList(MaterialCalendarView calendar) {
        calendar.setOnDateChangedListener((widget, date, selected) -> {
            if (dayViewModel.checkIsTaskListView(DayViewModel.getRefToVpPager().getValue()))
                dayViewModel.checkDateIsToday(date,
                        DayViewModel.getRefToFabDay().getValue(),
                        DayViewModel.getRefToDayNumberView().getValue());
            updateDate(date);
        });
    }

    public void updateDate(CalendarDay date) {
        var calendarDate = Calendar.getInstance();
        calendarDate.set(date.getYear(), date.getMonth(), date.getDay());
        mWordViewModel.getAllTaskDataFromDay(calendarDate.getTimeInMillis())
                .observe(this.getViewLifecycleOwner(),
                        taskData -> taskDayAdapter.setFilteredTaskList(taskData));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}