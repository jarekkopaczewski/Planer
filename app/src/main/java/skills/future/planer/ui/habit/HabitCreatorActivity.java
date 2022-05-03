package skills.future.planer.ui.habit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import skills.future.planer.R;
import skills.future.planer.databinding.ActivityHabitCreatorBinding;


public class HabitCreatorActivity extends AppCompatActivity {

    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
    private TextView timeEditText;
    private TextView editTextDateHabit;
    private ChipGroup daysChipGroupOne;
    private ChipGroup daysChipGroupTwo;
    private FloatingActionButton saveCreatorButtonHabit;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHabitCreatorBinding binding = ActivityHabitCreatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));

        // binding
        timeEditText = binding.timeEditText;
        editTextDateHabit = binding.editTextDateHabit;
        daysChipGroupOne = binding.daysChipGroupOne;
        daysChipGroupTwo = binding.daysChipGroupTwo;
        saveCreatorButtonHabit = binding.saveCreatorButtonHabit;
        editTextTitle = binding.editTextTitle;

        // set current time & date
        timeEditText.setText(formatter.format(calendar.getTime()));
        editTextDateHabit.setText(formatterDate.format(calendar2.getTime()));

        // sets first item selected in check boxes
        binding.spinner.selectItemByIndex(0);
        binding.spinner2.selectItemByIndex(0);

        // add on click & time change listener
        setUpTime();
        // add on click & time change listener
        setUpDate();
        // add save button listener & add conditions check
        saveHabitButtonSetUp();

        setTitle(R.string.habitsTitle);
    }

    /**
     * Adds on click listener to fab & check for incorrect data
     */
    private void saveHabitButtonSetUp() {
        saveCreatorButtonHabit.setOnClickListener(e -> {
            if (editTextTitle.getText() == null || editTextTitle.getText().length() <= 0) {
                Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            } else if (daysChipGroupOne.getCheckedChipIds().size() == 0 && daysChipGroupTwo.getCheckedChipIds().size() == 0) {
                Toast.makeText(this, R.string.habit_error_2, Toast.LENGTH_SHORT).show();
            } else {
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

        timeEditText.setOnClickListener(e -> new TimePickerDialog(this, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show());
    }

    /**
     * Creates OnDateSetListener and add it to date edit text
     */
    private void setUpDate() {
        DatePickerDialog.OnDateSetListener date = (datePicker, y, m, d) -> {
            calendar2.set(y, m, d);
            editTextDateHabit.setText(formatterDate.format(calendar2.getTime()));
        };

        editTextDateHabit.setOnClickListener(e -> new DatePickerDialog(this, date, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show());
    }
}