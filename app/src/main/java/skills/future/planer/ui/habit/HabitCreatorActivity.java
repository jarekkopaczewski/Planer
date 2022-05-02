package skills.future.planer.ui.habit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

import skills.future.planer.R;
import skills.future.planer.databinding.ActivityHabitCreatorBinding;


public class HabitCreatorActivity extends AppCompatActivity {

    private ActivityHabitCreatorBinding binding;
    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd:MM:yy", Locale.ENGLISH);
    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
    private TextView timeEditText;
    private TextView editTextDateHabit;
    private CalendarDay beginDay;
    private ChipGroup daysChipGroupOne;
    private ChipGroup daysChipGroupTwo;
    private FloatingActionButton saveCreatorButtonHabit;
    private EditText editTextTitle;
    private int hour = 12;
    private int min = 0;
    private int day = 0;
    private int month = 0;
    private int year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitCreatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));
        timeEditText = binding.timeEditText;
        editTextDateHabit = binding.editTextDateHabit;
        daysChipGroupOne = binding.daysChipGroupOne;
        daysChipGroupTwo = binding.daysChipGroupTwo;
        saveCreatorButtonHabit = binding.saveCreatorButtonHabit;
        editTextTitle = binding.editTextTitle;

        timeEditText.setText(formatter.format(calendar.getTime()));
        updateBeginDateEditText();

        year = calendar2.getTime().getYear();
        month = calendar2.getTime().getMonth();
        day = calendar2.getTime().getDay();
        calendar2.set(Calendar.YEAR, year);
        calendar2.set(Calendar.MONTH, month);
        calendar2.set(Calendar.DAY_OF_MONTH, day);
        updateBeginDateEditText();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TimePickerDialog.OnTimeSetListener time = (timePicker, hourOfDay, minute) -> {
            hour = hourOfDay;
            min = minute;
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            timeEditText.setText(formatter.format(calendar.getTime()));
        };

        DatePickerDialog.OnDateSetListener date = (datePicker, y, m, d) -> {
            calendar2.set(Calendar.YEAR, y);
            calendar2.set(Calendar.MONTH, m);
            calendar2.set(Calendar.DAY_OF_MONTH, d);
            day = d;
            month = m;
            year = y;
            updateBeginDateEditText();
        };

        timeEditText.setOnClickListener(e -> {
            new TimePickerDialog(this, time, hour, min, true).show();
        });

        editTextDateHabit.setOnClickListener(e -> {
            new DatePickerDialog(this, date, year, month, day).show();
        });

        PowerSpinnerView powerSpinnerView = binding.spinner;
        powerSpinnerView.selectItemByIndex(0);
        binding.spinner2.selectItemByIndex(0);

        saveCreatorButtonHabit.setOnClickListener(e -> {
            if (editTextTitle.getText() == null || editTextTitle.getText().length() <= 0) {
                Toast toast = Toast.makeText(this, "Tytuł nie może być pusty!", Toast.LENGTH_SHORT);
                toast.show();
            } else if (daysChipGroupOne.getCheckedChipIds().size() == 0 && daysChipGroupTwo.getCheckedChipIds().size() == 0) {
                Toast toast = Toast.makeText(this, "Wybierz przynajmniej jeden dzień!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                finish();
            }
        });


        setTitle("Kreator nawyków");
    }

    private void updateBeginDateEditText() {
        LocalDate date = calendar2.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        beginDay = CalendarDay.from(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String dateString = beginDay.getDay() + "." + beginDay.getMonth() + "." + beginDay.getYear();
        editTextDateHabit.setText(dateString);
    }

}