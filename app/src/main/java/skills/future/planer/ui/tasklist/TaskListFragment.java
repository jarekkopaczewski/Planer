package skills.future.planer.ui.tasklist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListBinding;
import skills.future.planer.ui.AnimateView;

public class TaskListFragment extends Fragment {

    private ListView listTotal;
    private TaskTotalAdapter taskTotalAdapter;
    private FragmentTaskListBinding binding;
    private TaskListModelView taskListModelView;

    public TaskListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        taskListModelView = new ViewModelProvider(this).get(TaskListModelView.class);
        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listTotal = binding.listTotalView;
        taskTotalAdapter = new TaskTotalAdapter(this.getContext(), inflater);
        listTotal.setAdapter(taskTotalAdapter);
        listTotal.setTextFilterEnabled(true);
        taskTotalAdapter.getFilter().filter("");

        // animation test
        AnimateView.singleAnimation(binding.fab, getContext(), R.anim.downup);

        binding.fab.setOnClickListener(view -> {
            AnimateView.animateInOut(binding.fab, getContext());
            Navigation.findNavController(view).navigate(TaskListFragmentDirections.actionNavTaskListToTaskListCreatorFragment());
        });

        binding.searchImageView.setOnClickListener(e -> {
            AnimateView.animateInOut(binding.searchImageView, getContext());
            taskTotalAdapter.getFilter().filter(binding.searchEditText.getText());
        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.searchEditText.getText().toString().equals("")) {
                    taskTotalAdapter.getFilter().filter("");
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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        taskTotalAdapter.refreshTaskList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}