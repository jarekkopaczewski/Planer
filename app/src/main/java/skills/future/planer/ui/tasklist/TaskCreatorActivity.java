package skills.future.planer.ui.tasklist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.stream.Collectors;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListCreatorBinding;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.month.MonthFragment;

public class TaskCreatorActivity extends AppCompatActivity {

    private final Calendar endingDayCalendar = Calendar.getInstance(), beginDayCalendar = Calendar.getInstance();
    private TaskData editTask;
    private FragmentTaskListCreatorBinding binding;
    private FloatingActionButton saveButton;
    private EditText endingDateEditText, beginDateEditText, taskTitleEditText, taskDetailsEditText;
    private CalendarDay endingDay, beginDay;
    private SwitchCompat switchDate, switchPriorities, switchTimePriorities, switchCategory;
    private PowerSpinnerView goalSpinner;
    private GoalData selectedGoal;
    private TaskDataViewModel taskDataViewModel;
    private Bundle parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task_list_creator);
        binding = FragmentTaskListCreatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));
        taskDataViewModel = new ViewModelProvider(this).get(TaskDataViewModel.class);

        editTask = new TaskData();

        this.parameters = getIntent().getExtras();
        setTitle(getString(R.string.task_creator_title));

        createEditDateFields();
        createSwitch();

        // binding
        saveButton = binding.saveCreatorButton;
        goalSpinner = binding.goalSpinner;
        taskTitleEditText = binding.EditTextTitle;
        taskDetailsEditText = binding.EditTextDetails;

        setStartingDateByGlobalDate();
        processFabColor();
        setUpGoals();

        // if parameters != null -> edit == true
        saveBtnOnClickListenerSetter(parameters != null);
        if (parameters != null) setUpValuesOnEdit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) showDialog();
        return false;
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded)
                .setIcon(R.drawable.warning)
                .setTitle(R.string.exit_activity_warning_1)
                .setMessage(R.string.exit_activity_warning_2)
                .setPositiveButton(R.string.agree, (dialog, which) -> finish())
                .setNegativeButton(R.string.disagree, null)
                .show();
    }

    private void setUpValuesOnEdit() {
        try {
            if (!parameters.containsKey("goalId")) {
                editTask = taskDataViewModel.findById(parameters.getLong("taskToEditId"));
                taskTitleEditText.setText(editTask.getTaskTitleText());
                taskDetailsEditText.setText(editTask.getTaskDetailsText());
                switchPriorities.setChecked(editTask.getPriorities() == Priorities.NotImportant);
                switchTimePriorities.setChecked(editTask.getTimePriority() == TimePriority.NotUrgent);
                switchCategory.setChecked(editTask.getCategory() == TaskCategory.Work);
                // ustawianie celi jest w observerze bo szybciej wykonywało się uzypełnianie niż
                // wczytywała się baza
                if (editTask.getStartingDate() != 0) {
                    switchDate.setChecked(true);
                    var beginDate = editTask.getStartingCalendarDate();
                    var endingDate = editTask.getEndingCalendarDate();
                    beginDayCalendar.set(beginDate.getYear(), beginDate.getMonth() - 1, beginDate.getDay());
                    endingDayCalendar.set(endingDate.getYear(), endingDate.getMonth() - 1, endingDate.getDay());
                    endingDateEditText.setText(DatesParser.toLocalDate(editTask.getEndingDate())
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    beginDateEditText.setText(DatesParser.toLocalDate(editTask.getStartingDate())
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
                setGoal();
                setTitle(getString(R.string.task_editor_title));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets goals from database and pass it to spinner
     * Creates listener to goal selection
     */
    private void setUpGoals() {
        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        goalsViewModel.getAllGoals().observe(this, goalData -> {
            var list = goalData.stream().map(GoalData::getTitle).collect(Collectors.toList());
            list.add(0, "brak");
            goalSpinner.setItems(list);
            try {
                int selected = parameters.getInt("goalId");
                goalSpinner.selectItemByIndex(selected + 1);
            } catch (NullPointerException | IndexOutOfBoundsException exp) {
                exp.printStackTrace();
            }
        });

        goalSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (i, s, i1, t1) -> {
            String goalText = (String) goalSpinner.getText();
            goalsViewModel.getAllGoals().observe(this, goalData -> selectedGoal =
                    goalData.stream().filter(item -> item.getTitle().equals(goalText)).findAny().orElse(null));
        });
    }

    /**
     * Sets the date based on the global date
     */
    private void setStartingDateByGlobalDate() {
        switchDate.setChecked(true);
        int y = MonthFragment.getGlobalSelectedDate().getYear(),
                m = MonthFragment.getGlobalSelectedDate().getMonth() - 1,
                d = MonthFragment.getGlobalSelectedDate().getDay();
        beginDayCalendar.set(y, m, d);
        endingDayCalendar.set(y, m, d);

        updateBeginDateEditText();
        updateEndingDateEditText();
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
     * Changes color of fab button
     */
    private void processFabColor() {
        if (!switchPriorities.isChecked() && !switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("urgentImportant", this)));
        else if (switchPriorities.isChecked() && !switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("urgentNotImportant", this)));
        else if (!switchPriorities.isChecked() && switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("notUrgentImportant", this)));
        else if (switchPriorities.isChecked() && switchTimePriorities.isChecked())
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.getColorFromPreferences("notUrgentNotImportant", this)));
    }

    /**
     * Sets listener on save button
     * If new task is created
     * listener will setFragmentResult on request key: requestKey, bundle key is: bundleKey
     * then it will back up
     * But if task is edited
     * listener sends edited TaskData
     */
    private void saveBtnOnClickListenerSetter(boolean edit) {
        saveButton.setOnClickListener(view1 -> {
            AnimateView.animateInOut(saveButton, this);
            if (checkTitle(saveButton)) {
                if (switchDate.isChecked()) {
                    if (checkDateDependency()) {
                        setTaskData();
                        editTask.setEndingCalendarDate(endingDay);
                        editTask.setStartingCalendarDate(beginDay);
                        if (selectedGoal != null)
                            editTask.setForeignKeyToGoal(selectedGoal.getGoalId());
                        if (goalSpinner.getSelectedIndex() == 0)
                            editTask.setForeignKeyToGoal(null);
                        sendTaskToDataBase(edit);
                    }
                } else {
                    setTaskData();
                    editTask.setEndingDate(0);
                    editTask.setStartingDate(0);
                    sendTaskToDataBase(edit);
                }
            }
        });
    }

    /**
     * Gets data from creator components and sets to task
     */
    private void setTaskData() {
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
    private void sendTaskToDataBase(boolean edit) {
        if (edit) taskDataViewModel.edit(editTask);
        else taskDataViewModel.insert(editTask);
        finish();
    }

    /**
     * Checks title is empty
     *
     * @param saveButton instance of button
     * @return true if title is not empty
     */
    private boolean checkTitle(FloatingActionButton saveButton) {
        AnimateView.animateInOut(saveButton, this);
        if (taskTitleEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.task_title_warning, Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog(this, date2,
                        beginDayCalendar.get(Calendar.YEAR),
                        beginDayCalendar.get(Calendar.MONTH),
                        beginDayCalendar.get(Calendar.DAY_OF_MONTH)
                ).show());
        endingDateEditText.setOnClickListener(view12 ->
                new DatePickerDialog(this, date,
                        endingDayCalendar.get(Calendar.YEAR),
                        endingDayCalendar.get(Calendar.MONTH),
                        endingDayCalendar.get(Calendar.DAY_OF_MONTH)
                ).show());
    }

    /**
     * Updates EndDateEditText
     */
    private void updateEndingDateEditText() {
        LocalDate date = endingDayCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        endingDay = CalendarDay.from(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        endingDateEditText.setText(DatesParser.toLocalDate(endingDayCalendar.getTimeInMillis())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    /**
     * Updates BeginDateEditText
     */
    private void updateBeginDateEditText() {
        LocalDate date = beginDayCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        beginDay = CalendarDay.from(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        beginDateEditText.setText(DatesParser.toLocalDate(beginDayCalendar.getTimeInMillis())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    /**
     * Checks is starting and ending dates are correct
     *
     * @return true if dates are correct
     */
    private boolean checkDateDependency() {
        if (beginDay == null) {
            Toast.makeText(this, getString(R.string.empty_begin_date_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endingDay == null) {
            Toast.makeText(this, getString(R.string.end_date_task_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkDate(endingDay, beginDay)) {
            Toast.makeText(this, getString(R.string.wrong_time_selection_error), Toast.LENGTH_SHORT).show();
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

    /**
     * Find index of task goal
     */
    private void setGoal() {
        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        goalsViewModel.getAllGoals().observe(this, goalData -> {
            var list = goalData.stream().map(GoalData::getTitle).collect(Collectors.toList());
            list.add(0, getString(R.string.empty));
            goalSpinner.setItems(list);
            try {
                goalSpinner.selectItemByIndex(list.indexOf(goalsViewModel.findById(editTask.getForeignKeyToGoal()).getTitle()));
            } catch (NullPointerException exp) {
                goalSpinner.selectItemByIndex(0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        processFabColor();
    }
}