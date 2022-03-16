package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.Calendar;
import android.widget.TextView;

import skills.future.planer.databinding.FragmentTaskListCreatorBinding;


public class TaskListCreatorFragment extends Fragment {

    private FragmentTaskListCreatorBinding binding;
    private TaskCreatorModelView taskCreatorModelView;
    private Button saveButton;
    private final Calendar myCalendar = Calendar.getInstance();
    private EditText editText;
    private Switch switchDate;
    public TaskListCreatorFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        taskCreatorModelView = new ViewModelProvider(this).get(TaskCreatorModelView.class);
        binding = FragmentTaskListCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.taskCreatorLabel;
        taskCreatorModelView.getText().observe(getViewLifecycleOwner(), textView::setText);

        switchDate = view.findViewById(R.id.SwitchDatePicker);
        switchDate.setChecked(false);
        switchDate.setOnCheckedChangeListener((compoundButton, b) -> {
            editText.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        });
        saveButton = view.findViewById(R.id.saveCreatorButton);
        saveBtnOnClickListenerSetter();
        editText = (EditText) view.findViewById(R.id.editTextDate);
        editTextSetter();
        return root;
    }
    private void saveBtnOnClickListenerSetter() {
        saveButton.setOnClickListener(view1 -> {
            Navigation.findNavController(view1)
                    .navigate(TaskListCreatorFragmentDirections.actionTaskListCreatorFragmentToNavTaskList());
        });
    }

    private void editTextSetter() {
        DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        };
        editText.setOnClickListener(view12 -> new DatePickerDialog(this.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel() {
//        Locale locale = new Locale("en", "UK");
//        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
//        dateFormatSymbols.setWeekdays(new String[]{
//                "Unused",
//                "Niedz",
//                "Pon",
//                "Wt",
//                "ŚR",
//                "Czw",
//                "Pt",
//                "Sob",
//        });
//
//        String pattern = "EEEEE MMMMM yyyy";
//        SimpleDateFormat simpleDateFormat =
//                new SimpleDateFormat(pattern, dateFormatSymbols);
        editText.setText(myCalendar.getTime().toString());
        //todo ustawić lepszy format daty
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}