package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
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
import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataDao;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;


public class TaskListCreatorFragment extends Fragment {

    private final Calendar endingDayCalendar = Calendar.getInstance(), beginDayCalendar = Calendar.getInstance();
    private TaskData editTask;
    private FragmentTaskListCreatorBinding binding;
    private TaskDataDao taskDataDao;
    private FloatingActionButton saveButton;
    private EditText endingDateEditText, beginDateEditText, taskTitleEditText, taskDetailsEditText;
    private CalendarDay endingDay, beginDay;
    private SwitchCompat switchDate, switchPriorities, switchTimePriorities, switchCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskListCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createEditDateFields();
        createSwitch();

        // save btn
        saveButton = binding.saveCreatorButton;

        // title and details edit texts
        taskTitleEditText = binding.EditTextTitle;
        taskDetailsEditText = binding.EditTextDetails;

        processFabColor();

        Integer taskID = -1;
        taskID = getTaskIDFromFragmentArgument(taskID);
        saveBtnOnClickListenerSetter(taskID);

        return root;
    }

    /**
     * Creates date edit texts
     */
    private void createEditDateFields() {
        beginDateEditText = binding.editTextBeginDate;
        beginDateEditText.setVisibility(View.INVISIBLE);

        endingDateEditText = binding.editTextEndDate;
        endingDateEditText.setVisibility(View.INVISIBLE);

        datePickers();
    }

    /**
     * Creates switch in TaskListCreatorFragment
     */
    private void createSwitch() {
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
        createSwitchIconListeners();
    }

    /**
     * Checks is edit of task or create new task
     */
    private Integer getTaskIDFromFragmentArgument(Integer taskID) {
        if (getArguments() != null) {
            taskID = (Integer) getArguments().get("idTaskToEdit");
            if (taskID != -1)
                loadDataFromTask(taskID);
        }
        return taskID;
    }

    /**
     * Creates switch listeners for changing icons
     */
    private void createSwitchIconListeners() {
        switchPriorities.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchPriorities.isChecked()) {
                binding.imageViewImportant.setImageResource(R.drawable.trash);
            } else binding.imageViewImportant.setImageResource(R.drawable.star);
            processFabColor();
        });

        switchTimePriorities.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchTimePriorities.isChecked()) {
                binding.imageViewTaskUrgent.setImageResource(R.drawable.snail);
            } else binding.imageViewTaskUrgent.setImageResource(R.drawable.fire);
            processFabColor();
        });

        switchCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchCategory.isChecked()) {
                binding.imageViewTaskDetails2.setImageResource(R.drawable.briefcase_2);
            } else binding.imageViewTaskDetails2.setImageResource(R.drawable.home_2);
            processFabColor();
        });
    }

    /**
     * Gets TaskData from database, and sets fields of creatorFragment
     */
    private void loadDataFromTask(Integer taskID) {
        taskDataDao = AppDatabase.getInstance(this.getContext()).taskDataTabDao();
        try {
            editTask = taskDataDao.findById(taskID);
            taskTitleEditText.setText(editTask.getTaskTitleText());
            taskDetailsEditText.setText(editTask.getTaskDetailsText());

            // types of task
            if (editTask.getPriorities() == Priorities.NotImportant)
                switchPriorities.setChecked(true);
            if (editTask.getTimePriority() == TimePriority.NotUrgent)
                switchTimePriorities.setChecked(true);
            if (editTask.getCategory() == TaskCategory.Work)
                switchCategory.setChecked(true);

            //dates
            if (editTask.getStartingDate() != 0) {
                switchDate.setChecked(true);
                var beginDate = editTask.getStartingCalendarDate();
                var endingDate = editTask.getEndingCalendarDate();
                beginDayCalendar.set(beginDate.getYear(), beginDate.getMonth() - 1, beginDate.getDay());
                endingDayCalendar.set(endingDate.getYear(), endingDate.getMonth() - 1, endingDate.getDay());
                updateBeginDateEditText();
                updateEndingDateEditText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes color of fab button
     */
    private void processFabColor() {
        if (!switchPriorities.isChecked() && !switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("urgentImportant", getContext())));
        else if (switchPriorities.isChecked() && !switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("urgentNotImportant", getContext())));
        else if (!switchPriorities.isChecked() && switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("notUrgentImportant", getContext())));
        else if (switchPriorities.isChecked() && switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("notUrgentNotImportant", getContext())));
    }

    /**
     * Sets listener on save button
     * If new task is created
     * listener will setFragmentResult on request key: requestKey, bundle key is: bundleKey
     * then it will back up
     * But if task is edited
     * listener sends edited TaskData
     */
    private void saveBtnOnClickListenerSetter(Integer taskID) {
        saveButton.setOnClickListener(view1 -> {
            AnimateView.animateInOut(saveButton, getContext());

            if (checkTitle(saveButton)) {
                if (switchDate.isChecked()) {
                    if (checkDateDependency()) {
                        setTaskData();
                        editTask.setEndingCalendarDate(endingDay);
                        editTask.setStartingCalendarDate(beginDay);
                        sendTaskToDataBase(taskID, view1);
                    }
                } else {
                    setTaskData();
                    editTask.setEndingDate(0);
                    editTask.setStartingDate(0);
                    sendTaskToDataBase(taskID, view1);
                }
            }
        });
    }

    /**
     * Gets data from creator components and sets to task
     */
    private void setTaskData() {
        if (editTask == null)
            editTask = new TaskData();
        editTask.setAllDataWithoutDates(
                switchCategory.isChecked() ? TaskCategory.Work : TaskCategory.Private,
                switchPriorities.isChecked() ? Priorities.NotImportant : Priorities.Important,
                switchTimePriorities.isChecked() ? TimePriority.NotUrgent : TimePriority.Urgent,
                taskTitleEditText.getText().toString(),
                taskDetailsEditText.getText().toString());
    }

    /**
     * Sends task to database depending on the creator type
     */
    private void sendTaskToDataBase(Integer taskID, View view1) {
        //If the task is edited
        if (taskID != -1)
            taskDataDao.editOne(editTask);
        else { //If the task is created
            Bundle result = new Bundle();
            result.putParcelable("bundleKey", editTask);
            getParentFragmentManager().setFragmentResult("requestKey", result);
        }
        Navigation.findNavController(view1).navigateUp();
    }


    /**
     * Checks title is empty
     *
     * @param saveButton instance of button
     * @return true if title is not empty
     */
    private boolean checkTitle(FloatingActionButton saveButton) {
        AnimateView.animateInOut(saveButton, getContext());

        if (taskTitleEditText.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this.getContext(),
                    "Tytuł nie może być pusty!",
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * Method sets OnClickListeners on editTextBeginDate and editTextEndDate
     * listener will show calendar popup
     */
    private void datePickers() {
        DatePickerDialog.OnDateSetListener date = (datePicker, y, m, d) -> {
            endingDayCalendar.set(Calendar.YEAR, y);
            endingDayCalendar.set(Calendar.MONTH, m);
            endingDayCalendar.set(Calendar.DAY_OF_MONTH, d);
            updateEndingDateEditText();
        };
        DatePickerDialog.OnDateSetListener date2 = (datePicker, y, m, d) -> {
            beginDayCalendar.set(Calendar.YEAR, y);
            beginDayCalendar.set(Calendar.MONTH, m);
            beginDayCalendar.set(Calendar.DAY_OF_MONTH, d);
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
     * Updates EndDateEditText
     */
    private void updateEndingDateEditText() {
        LocalDate date = endingDayCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        endingDay = CalendarDay.from(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String dateString = endingDay.getDay() + "." +
                endingDay.getMonth() + "." +
                endingDay.getYear();
        endingDateEditText.setText(dateString);
    }

    /**
     * Updates BeginDateEditText
     */
    private void updateBeginDateEditText() {
        LocalDate date = beginDayCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        beginDay = CalendarDay.from(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String dateString = beginDay.getDay() + "." +
                beginDay.getMonth() + "." +
                beginDay.getYear();
        beginDateEditText.setText(dateString);
    }

    /**
     * Checks is starting and ending dates are correct
     *
     * @return true if dates are correct
     */
    private boolean checkDateDependency() {

        if (beginDay == null) {
            Toast.makeText(this.getContext(),
                    "Data rozpoczęcia zadania nie może być pusta",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endingDay == null) {
            Toast.makeText(this.getContext(),
                    "Data zakończenia zadania nie może być pusta",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkDate(endingDay, beginDay)) {
            Toast.makeText(this.getContext(),
                    "Data zakończenia zadania nie może być wcześniej niż data jego rozpoczęcia!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkDate(endingDay, CalendarDay.today())) {
            Toast.makeText(this.getContext(),
                    "Data zakończenia zadania nie może być wcześniej niż dzisiaj!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkDate(beginDay, CalendarDay.today())) {
            Toast.makeText(this.getContext(),
                    "Data rozpoczęcia zadania nie może być wcześniej niż dzisiaj!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * @return true if firstDate is equal or before secondDate, false if after
     */
    private boolean checkDate(CalendarDay firstDate, CalendarDay secondDate) {
        return firstDate.isBefore(secondDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}