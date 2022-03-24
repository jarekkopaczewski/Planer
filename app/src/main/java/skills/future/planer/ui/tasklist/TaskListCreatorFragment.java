package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import skills.future.planer.databinding.FragmentTaskListCreatorBinding;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.database.TaskDataTable;
import skills.future.planer.db.task.enums.priority.TimePriority;


public class TaskListCreatorFragment extends Fragment {

    private FragmentTaskListCreatorBinding binding;
    private Button saveButton;
    private final Calendar endingDayCalendar = Calendar.getInstance(), beginDayCalendar = Calendar.getInstance();
    private EditText endingDateEditText, beginDateEditText, taskTitleEditText, taskDetailsEditText;
    private CalendarDay endingCalendarDay, beginCalendarDay = CalendarDay.today();
    private SwitchCompat switchDate, switchPriorities, switchTimePriorities, switchCategory;

    public TaskListCreatorFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskListCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        beginDateEditText = binding.editTextBeginDate;
        beginDateEditText.setVisibility(View.INVISIBLE);
        endingDateEditText = binding.editTextEndDate;
        endingDateEditText.setVisibility(View.INVISIBLE);
        switchPriorities = binding.switchImportant;
        switchTimePriorities = binding.switchUrgent;
        switchCategory = binding.switchCategory;
        switchDate = binding.SwitchDatePicker;
        switchDate.setChecked(false);
        switchDate.setOnCheckedChangeListener((compoundButton, b) -> {
                    beginDateEditText.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                    endingDateEditText.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                }
        );
        saveButton = binding.saveCreatorButton;
        saveBtnOnClickListenerSetter();

        editTextSetter();
        taskTitleEditText = binding.EditTextTitle;
        taskDetailsEditText = binding.EditTextDetails;
        return root;
    }

    private void saveBtnOnClickListenerSetter() {
        saveButton.setOnClickListener(view1 -> {
            TaskData data;
            TaskDataTable taskDataTable = new TaskDataTable(this.getContext());
            data = new TaskData(
                    switchCategory.isChecked() ? TaskCategory.Private : TaskCategory.Work,
                    switchPriorities.isChecked() ? Priorities.NotImportant : Priorities.Important,
                    switchTimePriorities.isChecked() ? TimePriority.NotUrgent : TimePriority.Urgent,
                    taskTitleEditText.getText().toString(),
                    taskDetailsEditText.getText().toString());
            if (switchDate.isChecked()) {
                data.setEndingDate(endingCalendarDay);
                data.setStartingDate(beginCalendarDay);
            }
            if (taskDataTable.addOne(data))
                data.setTaskDataId(taskDataTable.getIdOfLastAddedTask());

            Navigation.findNavController(view1)
                    .navigate(TaskListCreatorFragmentDirections
                            .actionTaskListCreatorFragmentToNavTaskList());
        });
    }

    private void editTextSetter() {
        DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            endingDayCalendar.set(Calendar.YEAR, i);
            endingDayCalendar.set(Calendar.MONTH, i1);
            endingDayCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateEndingDateEditText();
        };
        DatePickerDialog.OnDateSetListener date2 = (datePicker, i, i1, i2) -> {
            beginDayCalendar.set(Calendar.YEAR, i);
            beginDayCalendar.set(Calendar.MONTH, i1);
            beginDayCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateBeginDateEditText();
        };
        beginDateEditText.setOnClickListener(view12 ->
                new DatePickerDialog(this.getContext(), date2,
                        beginDayCalendar.get(Calendar.YEAR),
                        beginDayCalendar.get(Calendar.MONTH),
                        beginDayCalendar.get(Calendar.DAY_OF_MONTH)
                ).show());
        endingDateEditText.setOnClickListener(view12 ->
                new DatePickerDialog(this.getContext(), date,
                        endingDayCalendar.get(Calendar.YEAR),
                        endingDayCalendar.get(Calendar.MONTH),
                        endingDayCalendar.get(Calendar.DAY_OF_MONTH)
                ).show());
    }

    private void updateEndingDateEditText() {
        LocalDate date = endingDayCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        int day = date.getDayOfMonth(), month = date.getMonthValue(),
                year = date.getYear();
        endingCalendarDay = CalendarDay.from(year, month, day);
        String dateString = endingCalendarDay.getDay() + "." +
                endingCalendarDay.getMonth() + "." +
                endingCalendarDay.getYear();
        endingDateEditText.setText(dateString);
    }

    private void updateBeginDateEditText() {
        LocalDate date = beginDayCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        int day = date.getDayOfMonth(), month = date.getMonthValue(),
                year = date.getYear();
        beginCalendarDay = CalendarDay.from(year, month, day);
        String dateString =beginCalendarDay.getDay() + "." +
                beginCalendarDay.getMonth() + "." +
                beginCalendarDay.getYear();
        beginDateEditText.setText(dateString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}