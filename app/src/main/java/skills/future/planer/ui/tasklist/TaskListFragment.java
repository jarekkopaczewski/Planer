package skills.future.planer.ui.tasklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Random;

import skills.future.planer.databinding.FragmentTaskListBinding;

public class TaskListFragment extends Fragment {

    private ListView listTotal;
    private TaskTotalAdapter taskTotalAdapter;
    private FragmentTaskListBinding binding;
    private TaskListModelView taskListModelView;

    public TaskListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        taskListModelView = new ViewModelProvider(this).get(TaskListModelView.class);
        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listTotal = binding.listTotalView;
        taskTotalAdapter = new TaskTotalAdapter(this.getContext(), inflater);
        listTotal.setAdapter(taskTotalAdapter);

        binding.fab.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(TaskListFragmentDirections.actionNavTaskListToTaskListCreatorFragment());
            taskTotalAdapter.addItemToList(new TaskData(this.getContext(), new Random().nextInt() % 2));
            listTotal.setAdapter(taskTotalAdapter);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}