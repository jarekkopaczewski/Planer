package skills.future.planer.ui.tasklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import skills.future.planer.databinding.FragmentTaskListCreatorBinding;


public class TaskListCreatorFragment extends Fragment {

    private FragmentTaskListCreatorBinding binding;
    private TaskCreatorModelView taskCreatorModelView;

    public TaskListCreatorFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        taskCreatorModelView = new ViewModelProvider(this).get(TaskCreatorModelView.class);
        binding = FragmentTaskListCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.taskCreatorLabel;
        taskCreatorModelView.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}