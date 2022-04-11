package skills.future.planer.ui.tasklist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
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
    private ArrayList<String> names;

    public TaskListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listTotal = binding.listTotalView;
        taskTotalAdapter = new TaskTotalAdapter(this.getContext());
        listTotal.setAdapter(taskTotalAdapter);
        //listTotal.setTextFilterEnabled(true);
        //taskTotalAdapter.getFilter().filter("");
        listTotal.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mWordViewModel = ViewModelProviders.of(this).get(TaskDataViewModel.class);
        mWordViewModel.getAllTaskData().observe(this.getViewLifecycleOwner(), taskData -> taskTotalAdapter.setFilteredTaskList(taskData));


        // animation test
        AnimateView.singleAnimation(binding.fab, getContext(), R.anim.downup);

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            TaskData result = bundle.getParcelable("bundleKey");
            mWordViewModel.insert(result);
        });
        binding.fab.setOnClickListener(view -> {
            AnimateView.animateInOut(binding.fab, getContext());
            Navigation.findNavController(view).navigate(TaskListFragmentDirections.actionNavTaskListToTaskListCreatorFragment(-1));
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

        helper.attachToRecyclerView(listTotal);

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

        /**
         * Chips OnClick Listeners
         */
        binding.workChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.privateChip.isChecked())
                    binding.privateChip.setChecked(false);
            }
        });

        binding.privateChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.workChip.isChecked())
                    binding.workChip.setChecked(false);
            }
        });

        binding.importantChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.notImportantChip.isChecked())
                    binding.notImportantChip.setChecked(false);
            }
        });

        binding.notImportantChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.importantChip.isChecked())
                    binding.importantChip.setChecked(false);
            }
        });

        binding.urgentChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.notUrgentChip.isChecked())
                    binding.notUrgentChip.setChecked(false);
            }
        });

        binding.notUrgentChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.urgentChip.isChecked())
                    binding.urgentChip.setChecked(false);
            }
        });

        /**
         * Chip Group Listener
         * Searches by iDs and returns list of checked filters
         */
        binding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @SneakyThrows
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                List<Integer> checked = binding.chipGroup.getCheckedChipIds();
                names = new ArrayList<>();
                int work = binding.workChip.getId();
                int private_chip = binding.privateChip.getId();
                int urgent = binding.urgentChip.getId();
                int not_urgent = binding.notUrgentChip.getId();
                int important = binding.importantChip.getId();
                int not_important = binding.notImportantChip.getId();
               // System.out.println(work+"workId");

                for (Integer id : checked) {
                   // Chip chip = binding.chipGroup.findViewById(id);

                    if(id.equals(work)) names.add(TaskCategory.Work.toString());
                    if(id.equals(private_chip)) names.add(TaskCategory.Private.toString());
                    if(id.equals(urgent)) names.add(TimePriority.Urgent.toString());
                    if(id.equals(not_urgent)) names.add(TimePriority.NotUrgent.toString());
                    if(id.equals(important)) names.add(Priorities.Important.toString());
                    if(id.equals(not_important)) names.add(Priorities.NotImportant.toString());


                }

                taskTotalAdapter.CategoryFilter(names);

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