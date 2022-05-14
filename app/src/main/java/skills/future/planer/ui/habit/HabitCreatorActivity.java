package skills.future.planer.ui.habit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private int selectedGoalId = 0;
    private GoalData selectedGoal;

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

        Bundle parameters = getIntent().getExtras();
        // sets first item selected in check boxes
        habitDurationSpinner = binding.spinner;

        goalsViewModel.getAllGoals().observe(this, goalData -> {
            var list = goalData.stream().map(GoalData::getTitle).collect(Collectors.toList());
            list.add(0, "brak");
            goalSpinner.setItems(list);
            goalSpinner.selectItemByIndex(0);
        });

        goalSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (i, s, i1, t1) -> {
            selectedGoalId = i;
            System.out.println(goalSpinner.getText());
            String goalText = (String) goalSpinner.getText();
            goalsViewModel.getAllGoals().observe(this, goalData -> {
                // var list = goalData.stream().filter(item -> item.getTitle().equals(goalText)).findAny();
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
                HabitData habit = habitViewModel.findById(parameters.getLong("habitToEditId"));
                int hours = (int) (habit.getNotificationTime() / 3600000);
                int minute = (int) ((habit.getNotificationTime() / 60000) % 60);

                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minute);
                editTextTitle.setText(habit.getTitle());
                timeEditText.setText(formatter.format(calendar.getTime()));
                editTextDateHabit.setText(DatesParser.toLocalDate(habit.getBeginCalendarDay())
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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
                saveButtonOnActionWhenEditing(habit);
                setTitle("Edytor Nawyków");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // set current time & date
            calendar2.set(MonthFragment.getGlobalSelectedDate().getYear(),
                    MonthFragment.getGlobalSelectedDate().getMonth() - 1,
                    MonthFragment.getGlobalSelectedDate().getDay());
            timeEditText.setText(formatter.format(calendar.getTime()));
            editTextDateHabit.setText(formatterDate.format(calendar2.getTime()));


            // add save button listener & add conditions check
            saveHabitButtonSetUp();

            setTitle(R.string.habitsTitle);

            habitDurationSpinner.selectItemByIndex(0);
        }

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
                checkHabitsNumber();
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

                    if(selectedGoal!=null) {
                        habit.setForeignKeyToGoal(selectedGoal.getGoalId());
                        System.out.println("YAY");
                    }

                    habitViewModel.insert(habit);
                } catch (Exception dataBaseException) {
                    dataBaseException.printStackTrace();
                }
                finish();
            }
        });
    }

    private void saveButtonOnActionWhenEditing(HabitData habitData) {
        saveCreatorButtonHabit.setOnClickListener(e -> {
            if (editTextTitle.getText() == null || editTextTitle.getText().length() <= 0) {
                Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            } else if (daysChipGroupOne.getCheckedChipIds().size() == 0
                    && daysChipGroupTwo.getCheckedChipIds().size() == 0) {
                Toast.makeText(this, R.string.habit_error_2, Toast.LENGTH_SHORT).show();
            } else {
                checkHabitsNumber();
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
                    if(selectedGoal!=null) {
                        habitData.setForeignKeyToGoal(selectedGoal.getGoalId());
                        System.out.println("YAY");
                    }
                    habitViewModel.edit(habitData);
                } catch (Exception dataBaseException) {
                    dataBaseException.printStackTrace();
                }
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
        habitViewModel.getAllHabits().observe(this, habit -> {
            var list = new ArrayList<>(habit);
            try {
                if ((list.size() > 3)) {
                    Toast.makeText(this, R.string.reminder_too_many_habits, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}