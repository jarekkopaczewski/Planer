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

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;


import lombok.SneakyThrows;
import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;

public class TaskListFragment extends Fragment {

    private RecyclerView listTotal;
    private TaskTotalAdapter taskTotalAdapter;
    private FragmentTaskListBinding binding;
    private TaskDataViewModel mWordViewModel;
    private ArrayList<String> filters;

    public TaskListFragment() {
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mWordViewModel = new ViewModelProvider(this).get(TaskDataViewModel.class);
        taskTotalAdapter = new TaskTotalAdapter(this.getContext(), this.getActivity());

        listTotal = binding.listTotalView;
        listTotal.setAdapter(taskTotalAdapter);

       // mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter.setFilteredTaskList(taskData));

        //list of filters
        filters = new ArrayList<>();
        //filter on not done tasks on start
        filters.add("NotDone");
        binding.notDoneTask.setChecked(true);

        mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), listTotal -> {
            try {
                taskTotalAdapter.setFilteredTaskList(listTotal);
                taskTotalAdapter.CategoryFilter(filters);
                taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listTotal.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));

        // fab enter animation
        AnimateView.singleAnimation(binding.fab, getContext(), R.anim.downup);

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel.insert(result);
        });

        binding.fab.setOnClickListener(view -> {
            AnimateView.animateInOut(binding.fab, getContext());
            this.requireContext().startActivity(new Intent(this.getContext(), TaskCreatorActivity.class));
        });

        binding.searchImageView.setOnClickListener(e -> {
            AnimateView.animateInOut(binding.searchImageView, this.getContext());
            taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
        });

//        binding.searchEditText.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                binding.searchEditText.setText("");
//            }
//        });


        binding.searchEditText.addTextChangedListener(new TextWatcher() {

            @SneakyThrows
            @Override
            public void afterTextChanged(Editable s) {
                if (binding.searchEditText.getText().toString().equals("")) {
                    taskTotalAdapter.getFilter().filter("");
                }else {
                    taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
                }
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

        /*
         * Chips OnClick Listeners
         */
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
        ChipGroup.OnCheckedStateChangeListener listener = (group, checkedIds) -> {

            //list of checked chips ids
            List<Integer> checked = binding.chipGroup.getCheckedChipIds();

            //filters = new ArrayList<>();
            filters.clear();

            //getting chips ids
            int work = binding.workChip.getId();
            int private_chip = binding.privateChip.getId();
            int urgent = binding.urgentChip.getId();
            int not_urgent = binding.notUrgentChip.getId();
            int important = binding.importantChip.getId();
            int not_important = binding.notImportantChip.getId();
            int notstatus = binding.notDoneTask.getId();
            int status = binding.doneTask.getId();

            //compare them with checked ids
            for (Integer id : checked) {
                if (id.equals(work)) filters.add(TaskCategory.Work.toString());
                if (id.equals(private_chip)) filters.add(TaskCategory.Private.toString());
                if (id.equals(urgent)) filters.add(TimePriority.Urgent.toString());
                if (id.equals(not_urgent)) filters.add(TimePriority.NotUrgent.toString());
                if (id.equals(important)) filters.add(Priorities.Important.toString());
                if (id.equals(not_important)) filters.add(Priorities.NotImportant.toString());
                if (id.equals(notstatus)) filters.add("NotDone");
                if (id.equals(status)) filters.add("Done");
            }
            // System.out.println(filters);

            //give list of filters to CategoryFilter
            try {
                taskTotalAdapter.CategoryFilter(filters);
                if (binding.searchEditText.getText().toString().equals("")) {
                    taskTotalAdapter.getFilter().filter("");
                } else {
                    taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        binding.chipGroup.setOnCheckedStateChangeListener(listener);


        return root;
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