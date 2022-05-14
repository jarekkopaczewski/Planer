package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListCreatorBinding;
import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataDao;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.month.MonthFragment;


public class TaskListCreatorFragment extends Fragment {

    private final Calendar endingDayCalendar = Calendar.getInstance(), beginDayCalendar = Calendar.getInstance();
    private TaskData editTask;
    private FragmentTaskListCreatorBinding binding;
    private TaskDataDao taskDataDao;
    private FloatingActionButton saveButton;
    private EditText endingDateEditText, beginDateEditText, taskTitleEditText, taskDetailsEditText;
    private CalendarDay endingDay, beginDay;
    private SwitchCompat switchDate, switchPriorities, switchTimePriorities, switchCategory;
    private PowerSpinnerView goalSpinner;
    private GoalData selectedGoal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskListCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createEditDateFields();
        createSwitch();

        // save btn
        saveButton = binding.saveCreatorButton;

         goalSpinner = binding.goalSpinner;


        // title and details edit texts
        taskTitleEditText = binding.EditTextTitle;
        taskDetailsEditText = binding.EditTextDetails;

        setStartingDateByGlobalDate();

        processFabColor();
        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        goalsViewModel.getAllGoals().observe(getViewLifecycleOwner(), goalData -> {
            var list = goalData.stream().map(GoalData::getTitle).collect(Collectors.toList());
            list.add(0,"brak");
            goalSpinner.setItems(list);
        });

        goalSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (i, s, i1, t1) -> {
            String goalText = (String) goalSpinner.getText();
            goalsViewModel.getAllGoals().observe(getViewLifecycleOwner(), goalData -> {
               selectedGoal = goalData.stream().filter(item -> item.getTitle().equals(goalText)).findAny().orElse(null);
            });
        });

        Long taskID = -1L;
        taskID = getTaskIDFromFragmentArgument(taskID);
        saveBtnOnClickListenerSetter(taskID);

        return root;
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
     * Checks is edit of task or create new task
     */
    private Long getTaskIDFromFragmentArgument(Long taskID) {
        if (getArguments() != null) {
            taskID = (Long) getArguments().get("idTaskToEdit");
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
    private void loadDataFromTask(Long taskID) {
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

            setGoal();

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
    private void saveBtnOnClickListenerSetter(Long taskID) {
        saveButton.setOnClickListener(view1 -> {
            AnimateView.animateInOut(saveButton, getContext());

            if (checkTitle(saveButton)) {
                if (switchDate.isChecked()) {
                    if (checkDateDependency()) {
                        setTaskData();
                        editTask.setEndingCalendarDate(endingDay);
                        editTask.setStartingCalendarDate(beginDay);
                        if(selectedGoal!=null) {
                            editTask.setForeignKeyToGoal(selectedGoal.getGoalId());
                        }
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
    private void sendTaskToDataBase(Long taskID, View view1) {
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
        return true;
    }

    /**
     * @return true if firstDate is equal or before secondDate, false if after
     */
    private boolean checkDate(CalendarDay firstDate, CalendarDay secondDate) {
        return firstDate.isBefore(secondDate);
    }

    /**
     * Sets saved goal in spinner
     */
    private void setGoal(){
        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        goalsViewModel.getAllGoals().observe(getViewLifecycleOwner(), goalData -> {
            var list = goalData.stream().map(GoalData::getTitle).collect(Collectors.toList());
            list.add(0,"brak");
            goalSpinner.setItems(list);
            GoalData goalData1 = goalsViewModel.findById(editTask.getForeignKeyToGoal());
            String title = null;
            if(goalData1!=null){
                title = goalsViewModel.findById(editTask.getForeignKeyToGoal()).getTitle();
            }
            if(title != null){
                String finalTitle = title;
                var find = IntStream.range(0,list.size())
                        .filter(i -> finalTitle.equals(list.get(i)))
                        .findAny()
                        .orElse(-1);

                if(find!=-1){
                    goalSpinner.selectItemByIndex(find);
                }
            }else {
                goalSpinner.selectItemByIndex(0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        processFabColor();
    }
}