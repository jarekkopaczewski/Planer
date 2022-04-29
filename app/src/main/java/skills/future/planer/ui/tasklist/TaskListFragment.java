package skills.future.planer.ui.tasklist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import skills.future.planer.ui.slideshow.SlideshowViewModel;

public class TaskListFragment extends Fragment {

    private RecyclerView listTotal;
    private TaskTotalAdapter taskTotalAdapter;
    private FragmentTaskListBinding binding;
    private TaskDataViewModel mWordViewModel;
    private ArrayList<String> filters;

    public TaskListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mWordViewModel = new ViewModelProvider(this).get(TaskDataViewModel.class);
        taskTotalAdapter = new TaskTotalAdapter(this.getContext(), mWordViewModel);
        mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter.setFilteredTaskList(taskData));

        listTotal = binding.listTotalView;
        listTotal.setAdapter(taskTotalAdapter);
        //listTotal.setTextFilterEnabled(true);
//        taskTotalAdapter.getFilter().filter("");
        listTotal.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // fab enter animation
        AnimateView.singleAnimation(binding.fab, getContext(), R.anim.downup);

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel.insert(result);
        });
        binding.fab.setOnClickListener(view -> {
            //turn off filters
            binding.chipGroup.clearCheck();
            AnimateView.animateInOut(binding.fab, getContext());
            Navigation.findNavController(view).navigate(TaskListFragmentDirections.actionNavTaskListToTaskListCreatorFragment(-1));
        });

        binding.searchImageView.setOnClickListener(e -> {
            AnimateView.animateInOut(binding.searchImageView, this.getContext());
            //taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
//                if (binding.searchEditText.getText().toString().equals("")) {
//                    taskTotalAdapter.getFilter().filter("");
//                }else {
//                    taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
//                }
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

        /*
         * Chip Group Listener
         * Searches by iDs and returns list of checked filters
         */
        binding.chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {

            //list of checked chips ids
            List<Integer> checked = binding.chipGroup.getCheckedChipIds();

            //new list of filters
            filters = new ArrayList<>();

            //getting chips ids
            int work = binding.workChip.getId();
            int private_chip = binding.privateChip.getId();
            int urgent = binding.urgentChip.getId();
            int not_urgent = binding.notUrgentChip.getId();
            int important = binding.importantChip.getId();
            int not_important = binding.notImportantChip.getId();

            //compare them with checked ids
            for (Integer id : checked) {
                if (id.equals(work)) filters.add(TaskCategory.Work.toString());
                if (id.equals(private_chip)) filters.add(TaskCategory.Private.toString());
                if (id.equals(urgent)) filters.add(TimePriority.Urgent.toString());
                if (id.equals(not_urgent)) filters.add(TimePriority.NotUrgent.toString());
                if (id.equals(important)) filters.add(Priorities.Important.toString());
                if (id.equals(not_important)) filters.add(Priorities.NotImportant.toString());
            }

            //give list of filters to CategoryFilter
            try {
                taskTotalAdapter.CategoryFilter(filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}