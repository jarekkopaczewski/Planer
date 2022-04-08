package skills.future.planer.ui.tasklist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListBinding;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.ui.AnimateView;

public class TaskListFragment extends Fragment {

    private RecyclerView listTotal;
    private TaskTotalAdapter taskTotalAdapter;
    private FragmentTaskListBinding binding;
    private TaskDataViewModel mWordViewModel;

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
//        taskTotalAdapter.getFilter().filter("");
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
            Navigation.findNavController(view).navigate(TaskListFragmentDirections.actionNavTaskListToTaskListCreatorFragment());
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}