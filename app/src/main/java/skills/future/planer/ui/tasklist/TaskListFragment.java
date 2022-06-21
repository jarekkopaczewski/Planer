package skills.future.planer.ui.tasklist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lombok.SneakyThrows;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListBinding;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.tasklist.filters_chain.TaskCategoryFilter;
import skills.future.planer.ui.tasklist.filters_chain.TaskDoneFilter;
import skills.future.planer.ui.tasklist.filters_chain.TaskPriorityFilter;
import skills.future.planer.ui.tasklist.filters_chain.TaskTimePriorityFilter;

public class TaskListFragment extends Fragment {
    private TaskTotalAdapter taskTotalAdapter;
    private FragmentTaskListBinding binding;


    @SuppressLint({"SetTextI18n"})
    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskListBinding.inflate(inflater, container, false);

        taskTotalAdapter = new TaskTotalAdapter(this.getContext(), this.getActivity(), new TaskDoneFilter(false));
        binding.notDoneTask.setChecked(true);
        RecyclerView listTotal1 = binding.listTotalView;
        listTotal1.setAdapter(taskTotalAdapter);

        new ViewModelProvider(this).get(TaskDataViewModel.class).getAllTaskData()
                .observe(this.getViewLifecycleOwner(), listTotal -> {
                    taskTotalAdapter.setDisplayedList(listTotal);
                    taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
                });

        listTotal1.setLayoutManager(new LinearLayoutManager(this.getContext(),
                RecyclerView.VERTICAL, false));

        setUpFAB();

        binding.searchImageView.setOnClickListener(e -> {
            AnimateView.animateInOut(binding.searchImageView, this.getContext());
            taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {

            @SneakyThrows
            @Override
            public void afterTextChanged(Editable s) {
                taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        setUpChips();
        filtering();
        return binding.getRoot();
    }

    private void setUpFAB() {
        // fab enter animation
        AnimateView.singleAnimation(binding.fab, getContext(), R.anim.downup);

        binding.fab.setOnClickListener(view -> {
            AnimateView.animateInOut(binding.fab, getContext());
            this.requireContext().startActivity(new Intent(this.getContext(), TaskCreatorActivity.class));
        });
    }

    /**
     * Chips OnClick Listeners
     */
    private void setUpChips() {
        binding.workChip.setOnClickListener(view -> {
            if (binding.privateChip.isChecked())
                binding.privateChip.setChecked(false);
        });

        binding.privateChip.setOnClickListener(view -> {
            if (binding.workChip.isChecked())
                binding.workChip.setChecked(false);
        });

        binding.importantChip.setOnClickListener(view -> {
            if (binding.notImportantChip.isChecked())
                binding.notImportantChip.setChecked(false);
        });

        binding.notImportantChip.setOnClickListener(view -> {
            if (binding.importantChip.isChecked())
                binding.importantChip.setChecked(false);
        });

        binding.urgentChip.setOnClickListener(view -> {
            if (binding.notUrgentChip.isChecked())
                binding.notUrgentChip.setChecked(false);
        });

        binding.notUrgentChip.setOnClickListener(view -> {
            if (binding.urgentChip.isChecked())
                binding.urgentChip.setChecked(false);
        });

        binding.notDoneTask.setOnClickListener(view -> {
            if (binding.doneTask.isChecked())
                binding.doneTask.setChecked(false);
        });

        binding.doneTask.setOnClickListener(view -> {
            if (binding.notDoneTask.isChecked())
                binding.notDoneTask.setChecked(false);
        });

        /*
         * Chip Group Listener
         * Searches by iDs and returns list of checked filters
         */
        binding.chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filtering());
    }

    private void filtering() {
        taskTotalAdapter.cleanFilters();
        //compare them with checked ids
        for (Integer id : binding.chipGroup.getCheckedChipIds()) {
            if (id.equals(binding.workChip.getId()))
                taskTotalAdapter.addFilter(new TaskCategoryFilter(TaskCategory.Work));
            if (id.equals(binding.privateChip.getId()))
                taskTotalAdapter.addFilter(new TaskCategoryFilter(TaskCategory.Private));
            if (id.equals(binding.urgentChip.getId()))
                taskTotalAdapter.addFilter(new TaskTimePriorityFilter(TimePriority.Urgent));
            if (id.equals(binding.notUrgentChip.getId()))
                taskTotalAdapter.addFilter(new TaskTimePriorityFilter(TimePriority.NotUrgent));
            if (id.equals(binding.importantChip.getId()))
                taskTotalAdapter.addFilter(new TaskPriorityFilter(Priorities.Important));
            if (id.equals(binding.notImportantChip.getId()))
                taskTotalAdapter.addFilter(new TaskPriorityFilter(Priorities.NotImportant));
            if (id.equals(binding.notDoneTask.getId()))
                taskTotalAdapter.addFilter(new TaskDoneFilter(false));
            if (id.equals(binding.doneTask.getId()))
                taskTotalAdapter.addFilter(new TaskDoneFilter(true));
        }

        //give list of filters to CategoryFilter
        taskTotalAdapter.CategoryFilter();
        taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
    }

    @SneakyThrows
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @SneakyThrows
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}