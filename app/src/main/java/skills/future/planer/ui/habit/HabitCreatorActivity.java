package skills.future.planer.ui.habit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.stream.Collectors;

import skills.future.planer.R;
import skills.future.planer.databinding.ActivityHabitCreatorBinding;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.habit.HabitData;
import skills.future.planer.db.habit.HabitDuration;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.month.MonthFragment;


public class HabitCreatorActivity extends AppCompatActivity {

    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
    private TextView timeEditText;
    private TextView editTextDateHabit;
    private ChipGroup daysChipGroupOne;
    private ChipGroup daysChipGroupTwo;
    private FloatingActionButton saveCreatorButtonHabit;
    private EditText editTextTitle;
    private HabitViewModel habitViewModel;
    private PowerSpinnerView habitDurationSpinner;
    private PowerSpinnerView goalSpinner;
    private Chip MondayChip, TuesdayChip, WednesdayChip, ThursdayChip, FridayChip, SaturdayChip, SundayChip;
    private GoalData selectedGoal;
    private Bundle parameters;

    /**
     * HabitData object to be set/edited in activity
     */
    private HabitData habitData;

    /**
     * Clone of habitData used to compare
     */
    private HabitData openedHabitData;

    /**
     * True - habit is edited, false - habit is created
     */
    private boolean edition = false;

    /**
     * String of habit reminder time taken from textView
     */
    private String habit_reminder_time;

    /**
     * String of habit start date taken from textView
     */
    private String habit_date_start;


    public HabitCreatorActivity() {
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHabitCreatorBinding binding = ActivityHabitCreatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));

        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        // binding
        timeEditText = binding.timeEditText;
        editTextDateHabit = binding.editTextDateHabit;
        daysChipGroupOne = binding.daysChipGroupOne;
        daysChipGroupTwo = binding.daysChipGroupTwo;
        saveCreatorButtonHabit = binding.saveCreatorButtonHabit;
        editTextTitle = binding.editTextTitle;
        goalSpinner = binding.spinner2;

        parameters = getIntent().getExtras();
        // sets first item selected in check boxes
        habitDurationSpinner = binding.spinner;

        goalsViewModel.getAllGoals().observe(this, goalData -> {
            var list = goalData.stream().map(GoalData::getTitle).collect(Collectors.toList());
            list.add(0, "brak");
            goalSpinner.setItems(list);
        });

        goalSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (i, s, i1, t1) -> {
            String goalText = (String) goalSpinner.getText();
            goalsViewModel.getAllGoals().observe(this, goalData -> {
                selectedGoal = goalData.stream().filter(item -> item.getTitle().equals(goalText)).findAny().orElse(null);
            });
        });

        // add on click & time change listener
        setUpTime();
        // add on click & time change listener
        setUpDate();

        MondayChip = binding.MondayChip;
        TuesdayChip = binding.TuesdayChip;
        WednesdayChip = binding.WednesdayChip;
        ThursdayChip = binding.ThursdayChip;
        FridayChip = binding.FridayChip;
        SaturdayChip = binding.SaturdayChip;
        SundayChip = binding.SunDayChip;

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        if (parameters != null) {
            try {
                //parameters are loaded - habit is edited
                if (!parameters.containsKey("goalId")) {
                    edition = true;
                    HabitData habit = habitViewModel.findById(parameters.getLong("habitToEditId"));
                    int hours = (int) (habit.getNotificationTime() / 3600000);
                    int minute = (int) ((habit.getNotificationTime() / 60000) % 60);

                    calendar.set(Calendar.HOUR_OF_DAY, hours);
                    calendar.set(Calendar.MINUTE, minute);
                    editTextTitle.setText(habit.getTitle());
                    timeEditText.setText(formatter.format(calendar.getTime()));
                    editTextDateHabit.setText(DatesParser.toLocalDate(habit.getBeginCalendarDay())
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

                    //setting strings to compare
                    habit_reminder_time = formatter.format(calendar.getTime());
                    habit_date_start = DatesParser.toLocalDate(habit.getBeginCalendarDay())
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

                    var days = habit.getDaysOfWeek();
                    MondayChip.setChecked(days.charAt(0) == '1');
                    TuesdayChip.setChecked(days.charAt(1) == '1');
                    WednesdayChip.setChecked(days.charAt(2) == '1');
                    ThursdayChip.setChecked(days.charAt(3) == '1');
                    FridayChip.setChecked(days.charAt(4) == '1');
                    SaturdayChip.setChecked(days.charAt(5) == '1');
                    SundayChip.setChecked(days.charAt(6) == '1');
                    switch (habit.getHabitDuration()) {
                        case UltraShort -> habitDurationSpinner.selectItemByIndex(0);
                        case Short -> habitDurationSpinner.selectItemByIndex(1);
                        case Long -> habitDurationSpinner.selectItemByIndex(2);
                    }
                    calendar2.setTimeInMillis(habit.getBeginDay());
                    setGoal(habit);

                    //setting global field
                    habitData = habit;
                    //setting clone to compare
                    openedHabitData = habit.clone();

                    saveButtonOnActionWhenEditing();
                    setTitle("Edytor Nawyków");
                } else {
                    setUpDefaultHabit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setUpDefaultHabit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    /**
     * Shows warning dialog if edition has been detected
     */
    private void showDialog() {
        if (checkEdit()) {
            new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded)
                    .setIcon(R.drawable.warning)
                    .setTitle(R.string.exit_activity_warning_1)
                    .setMessage(R.string.exit_activity_warning_2)
                    .setPositiveButton(R.string.agree, (dialog, which) -> finish())
                    .setNegativeButton(R.string.disagree, null)
                    .show();
        } else finish();
    }


    private void setUpDefaultHabit() {
        // set current time & date
        calendar2.set(MonthFragment.getGlobalSelectedDate().getYear(),
                MonthFragment.getGlobalSelectedDate().getMonth() - 1,
                MonthFragment.getGlobalSelectedDate().getDay());
        timeEditText.setText(formatter.format(calendar.getTime()));
        editTextDateHabit.setText(formatterDate.format(calendar2.getTime()));
        //goalSpinner.setText(getResources().getString(R.string.noneGoal));
        setGoal(habitData);

        // add save button listener & add conditions check
        saveHabitButtonSetUp();

        setTitle(R.string.habitsTitle);

        habitDurationSpinner.selectItemByIndex(0);
    }

    /**
     * Adds on click listener to fab & check for incorrect data
     */
    private void saveHabitButtonSetUp() {
        saveCreatorButtonHabit.setOnClickListener(e -> {
            if (editTextTitle.getText() == null || editTextTitle.getText().length() <= 0) {
                Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            } else if (daysChipGroupOne.getCheckedChipIds().size() == 0 &&
                    daysChipGroupTwo.getCheckedChipIds().size() == 0) {
                Toast.makeText(this, R.string.habit_error_2, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    var tab = getResources().getStringArray(R.array.temp_array);
                    HabitDuration duration = switch (Integer.parseInt(tab[habitDurationSpinner
                            .getSelectedIndex()].split(" ")[0])) {
                        case 21 -> HabitDuration.UltraShort;
                        case 90 -> HabitDuration.Short;
                        case 120 -> HabitDuration.Long;
                        default -> throw new IllegalStateException("Unexpected value: " +
                                Integer.parseInt(tab[habitDurationSpinner.getSelectedIndex()].split(" ")[0]));
                    };
                    String weekDays = (MondayChip.isChecked() ? "1" : "0") +
                            (TuesdayChip.isChecked() ? "1" : "0") +
                            (WednesdayChip.isChecked() ? "1" : "0") +
                            (ThursdayChip.isChecked() ? "1" : "0") +
                            (FridayChip.isChecked() ? "1" : "0") +
                            (SaturdayChip.isChecked() ? "1" : "0") +
                            (SundayChip.isChecked() ? "1" : "0");
                    var habit = new HabitData(editTextTitle.getText().toString(), weekDays, duration,
                            DatesParser.toLocalDate(calendar2.getTime()),
                            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                    if (selectedGoal != null) {
                        habit.setForeignKeyToGoal(selectedGoal.getGoalId());
                    }
                    if (goalSpinner.getSelectedIndex() == 0) {
                        habit.setForeignKeyToGoal(null);
                    }

                    habitViewModel.insert(habit);
                    checkHabitsNumber();
                } catch (Exception dataBaseException) {
                    dataBaseException.printStackTrace();
                }
                finish();
            }
        });
    }

    /**
     * Saves habit if all HabitData fields are correct
     */
    private void saveButtonOnActionWhenEditing() {
        saveCreatorButtonHabit.setOnClickListener(e -> {
            if (loadDatafromActivity()) {
                habitViewModel.edit(habitData);
                finish();
            }
        });
    }

    /**
     * Creates OnTimeSetListener and add it to time edit text
     */
    private void setUpTime() {
        TimePickerDialog.OnTimeSetListener time = (timePicker, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            timeEditText.setText(formatter.format(calendar.getTime()));
        };

        timeEditText.setOnClickListener(e -> new TimePickerDialog(this,
                time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                true).show());
    }

    /**
     * Creates OnDateSetListener and add it to date edit text
     */
    private void setUpDate() {
        DatePickerDialog.OnDateSetListener date = (datePicker, y, m, d) -> {
            calendar2.set(y, m, d);
            editTextDateHabit.setText(formatterDate.format(calendar2.getTime()));
        };

        editTextDateHabit.setOnClickListener(e -> new DatePickerDialog(this,
                date, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void checkHabitsNumber() {
        if (habitViewModel.getAllHabitsList().size() > 3)
            Toast.makeText(this, R.string.reminder_too_many_habits, Toast.LENGTH_LONG).show();
    }

    private void setGoal(HabitData habitData) {
        GoalsViewModel goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        goalsViewModel.getAllGoals().observe(this, goalData -> {
            var list = goalData.stream().map(GoalData::getTitle).collect(Collectors.toList());
            list.add(0, getString(R.string.empty));
            goalSpinner.setItems(list);
            if (!edition && parameters != null) {
                try {
                    int selected = parameters.getInt("goalId");
                    goalSpinner.selectItemByIndex(selected + 1);
                    System.out.println(selected);
                } catch (NullPointerException | IndexOutOfBoundsException exp) {
                    exp.printStackTrace();
                }
            } else {
                try {
                    goalSpinner.selectItemByIndex(list.indexOf(goalsViewModel.findById(habitData.getForeignKeyToGoal()).getTitle()));
                } catch (NullPointerException exp) {
                    goalSpinner.selectItemByIndex(0);
                }
            }

        });
    }

    /**
     * Checks if data has been edited
     *
     * @return true - is edited, is not edited
     */
    private boolean checkEdit() {
        boolean isEdited = true;
        if (edition && loadDatafromActivity()) {
            isEdited = !(habitData.equals(openedHabitData) &&
                    habit_date_start.equals(editTextDateHabit.getText().toString()) &&
                    habit_reminder_time.equals(timeEditText.getText().toString()));
        }
        return isEdited;
    }

    /**
     * Loads and converts data from activity, sets habitData fields
     *
     * @return true - data have been set
     */
    private boolean loadDatafromActivity() {
        if (editTextTitle.getText() == null || editTextTitle.getText().length() <= 0) {
            Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (daysChipGroupOne.getCheckedChipIds().size() == 0
                && daysChipGroupTwo.getCheckedChipIds().size() == 0) {
            Toast.makeText(this, R.string.habit_error_2, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            try {
                var tab = getResources().getStringArray(R.array.temp_array);
                HabitDuration duration = switch (Integer.parseInt(tab[habitDurationSpinner
                        .getSelectedIndex()].split(" ")[0])) {
                    case 21 -> HabitDuration.UltraShort;
                    case 90 -> HabitDuration.Short;
                    case 120 -> HabitDuration.Long;
                    default -> throw new IllegalStateException("Unexpected value: " +
                            Integer.parseInt(tab[habitDurationSpinner.getSelectedIndex()].split(" ")[0]));
                };
                habitData.editHabitDur(duration);
                String weekDays = (MondayChip.isChecked() ? "1" : "0") +
                        (TuesdayChip.isChecked() ? "1" : "0") +
                        (WednesdayChip.isChecked() ? "1" : "0") +
                        (ThursdayChip.isChecked() ? "1" : "0") +
                        (FridayChip.isChecked() ? "1" : "0") +
                        (SaturdayChip.isChecked() ? "1" : "0") +
                        (SundayChip.isChecked() ? "1" : "0");
                habitData.editDaysOfWeek(weekDays);
                habitData.setTitle(editTextTitle.getText().toString());
                habitData.setBeginLocalDay(DatesParser.toLocalDate(calendar2.getTime()));
                habitData.setNotificationTime(
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
                if (selectedGoal != null) {
                    habitData.setForeignKeyToGoal(selectedGoal.getGoalId());
                }
                if (goalSpinner.getSelectedIndex() == 0) {
                    habitData.setForeignKeyToGoal(null);
                }
                return true;
            } catch (Exception dataBaseException) {
                dataBaseException.printStackTrace();
            }
        }
        return false;
    }
}
