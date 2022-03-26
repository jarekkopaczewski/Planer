package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListCreatorBinding;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.database.TaskDataTable;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;


public class TaskListCreatorFragment extends Fragment {

    private FragmentTaskListCreatorBinding binding;
    private FloatingActionButton saveButton;
    private final Calendar endingDayCalendar = Calendar.getInstance(), beginDayCalendar = Calendar.getInstance();
    private EditText endingDateEditText, beginDateEditText, taskTitleEditText, taskDetailsEditText;
    private CalendarDay endingCalendarDay, beginCalendarDay = CalendarDay.today();
    private SwitchCompat switchDate, switchPriorities, switchTimePriorities, switchCategory;

    public TaskListCreatorFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskListCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // date edit texts
        beginDateEditText = binding.editTextBeginDate;
        beginDateEditText.setVisibility(View.INVISIBLE);
        endingDateEditText = binding.editTextEndDate;
        endingDateEditText.setVisibility(View.INVISIBLE);
        updateBeginDateEditText();
        datePickers();
        // switches
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
        // save btn
        saveButton = binding.saveCreatorButton;

        saveBtnOnClickListenerSetter();
        // title and details edit texts
        taskTitleEditText = binding.EditTextTitle;
        taskDetailsEditText = binding.EditTextDetails;
        return root;
    }

    /**
     * Method is saveBtn On Click Listener
     * Listener will add new TaskData to database and back to listView
     */
    private void saveBtnOnClickListenerSetter() {
        saveButton.setOnClickListener(view1 -> {
            AnimateView.singleAnimation(saveButton, getContext(), R.anim.bounce);
            TaskDataTable taskDataTable = new TaskDataTable(this.getContext());
            TaskData data = new TaskData(
                    switchCategory.isChecked() ? TaskCategory.Private : TaskCategory.Work,
                    switchPriorities.isChecked() ? Priorities.NotImportant : Priorities.Important,
                    switchTimePriorities.isChecked() ? TimePriority.NotUrgent : TimePriority.Urgent,
                    taskTitleEditText.getText().toString(),
                    taskDetailsEditText.getText().toString());
            if (switchDate.isChecked()) {
                //if user want to add dates
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

    /**
     * Method sets OnClickListeners on editTextBeginDate and editTextEndDate
     * listener will show calendar popup
     */
    private void datePickers() {
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

    /**
     * Method update EndDateEditText
     * after check if date isn't earlier than endingCalendarDay it will set new date
     * if date isn't correct there will be generate toast with information that date is wrong
     */
    private void updateEndingDateEditText() {
        LocalDate date = endingDayCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        int day = date.getDayOfMonth(), month = date.getMonthValue(),
                year = date.getYear();
        CalendarDay chosenDay;
        chosenDay = CalendarDay.from(year, month, day);
        if (checkDate(chosenDay, beginCalendarDay)) {
            endingCalendarDay = chosenDay;
            String dateString = endingCalendarDay.getDay() + "." +
                    endingCalendarDay.getMonth() + "." +
                    endingCalendarDay.getYear();
            endingDateEditText.setText(dateString);
        } else {
            Toast toast = Toast.makeText(this.getContext(),
                    "Data zakończenia zadania nie może być wcześniej niż data jego rozpoczęcia!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Method update BeginDateEditText
     * after check if date isn't earlier than today it will set new date
     * if date isn't correct there will be generate toast with information that date is wrong
     */
    private void updateBeginDateEditText() {

        LocalDate date = beginDayCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        int day = date.getDayOfMonth(), month = date.getMonthValue(),
                year = date.getYear();
        CalendarDay chosenDay;
        chosenDay = CalendarDay.from(year, month, day);
        if (checkDate(chosenDay, CalendarDay.today())) {
            beginCalendarDay = chosenDay;
            String dateString = beginCalendarDay.getDay() + "." +
                    beginCalendarDay.getMonth() + "." +
                    beginCalendarDay.getYear();
            beginDateEditText.setText(dateString);
        } else {
            Toast toast = Toast.makeText(this.getContext(),
                    "Data rozpoczenia zadania nie może być wcześniej niż dzisiaj!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * @return true if beginCalendarDay is later then today or is today
     */
    private boolean checkDate(CalendarDay day, CalendarDay dayLandmark) {
        if (day.equals(dayLandmark))
            return true;
        if (day.getYear() > dayLandmark.getYear())
            return true;
        if (day.getYear() < dayLandmark.getYear())
            return false;
        if (day.getMonth() > dayLandmark.getMonth())
            return true;
        if (day.getMonth() < dayLandmark.getMonth())
            return false;
        return day.getDay() >= dayLandmark.getDay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}