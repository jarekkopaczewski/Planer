package skills.future.planer.ui.goals.creator;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.stream.Collectors;

import skills.future.planer.R;
import skills.future.planer.databinding.ActivityGoalsCreatorBinding;
import skills.future.planer.db.goal.GoalData;
import skills.future.planer.db.goal.GoalsViewModel;
import skills.future.planer.db.habit.HabitViewModel;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.goals.pager.recycler.MixedViewAdapter;

public class GoalsCreatorActivity extends AppCompatActivity {
    private final SimpleDateFormat formatterDate =
            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private final SimpleDateFormat formatter =
            new SimpleDateFormat("LLLL yyyy", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();
    private EditText titleEditText, detailEditText;
    private TextView editTextDateGoal;
    private GoalsViewModel goalsViewModel;
    private HabitViewModel habitViewModel;
    private TaskDataViewModel taskDataViewModel;
    private FloatingActionButton saveFAB;
    private RecyclerView recyclerView;
    private MixedViewAdapter mixedViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGoalsCreatorBinding binding = ActivityGoalsCreatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));

        setUpFields(binding);

        setTitle("Kreator celów");

        var goalsIdToEdit = getIntent().getExtras();
        if (goalsIdToEdit != null) {
            setTitle("Edytor celów");
            var goal = goalsViewModel.findById(goalsIdToEdit.getLong("goalIdToEdit"));
            titleEditText.setText(goal.getTitle());
            detailEditText.setText(goal.getDetails());
            calendar.setTimeInMillis(goal.getDate());
            saveFABSetUp(goal);
        } else {
            calendar.set(CalendarDay.today().getYear(),
                    CalendarDay.today().getMonth() - 1,
                    CalendarDay.today().getDay());
            saveFABSetUp();
        }
        editTextDateGoal.setText(formatter.format(calendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home){
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
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

    private void setUpFields(ActivityGoalsCreatorBinding binding) {
        titleEditText = binding.titleEditText;
        detailEditText = binding.detailsEditText;
        editTextDateGoal = binding.editTextDateGoal;
        saveFAB = binding.saveFABGoalCreator;
        goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        taskDataViewModel = new ViewModelProvider(this).get(TaskDataViewModel.class);
        mixedViewAdapter = new MixedAdapterInGoalsCreator(this, this);
        habitViewModel.getAllHabits()
                .observe(this, habitData -> {
                    if (habitData.size() > 0)
                        mixedViewAdapter.setHabitsList(habitData.stream()
                                .filter(habitData1 -> habitData1.getForeignKeyToGoal() == null)
                                .collect(Collectors.toList()));
                });
        taskDataViewModel.getAllTaskData()
                .observe(this, taskDataList -> {
                    if (taskDataList.size() > 0)
                        mixedViewAdapter.setFullTaskList(taskDataList.stream()
                                .filter(taskData -> taskData.getForeignKeyToGoal() == null)
                                .collect(Collectors.toList()));
                });
        recyclerView = binding.recyclerView2;
        recyclerView.setAdapter(mixedViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpDate();
    }

    private void setUpDate() {
        MonthPickerDialog.OnDateSetListener date = (y, m) -> {
            calendar.set(y, m, 1, 0, 0);
            editTextDateGoal.setText(formatter.format(calendar.getTime()));
        };


        editTextDateGoal.setOnClickListener(e ->  new MonthPickerDialog.Builder(this,
                (selectedMonth, selectedYear) -> {
                calendar.set(selectedYear, selectedMonth, 1, 0, 0);
                editTextDateGoal.setText(formatter.format(calendar.getTime()));
                }
                , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
                .setMinYear(CalendarDay.today().getYear())
                .setMaxYear(CalendarDay.today().getYear()+100)
                .setTitle(getResources().getString(R.string.goal_month_picker_title))
                .build()
                .show());
    }

    private void saveFABSetUp() {
        saveFAB.setOnClickListener(e -> {
            if (titleEditText.getText() == null || titleEditText.getText().length() <= 0)
                Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            else {
                if (calendar.get(Calendar.YEAR) == CalendarDay.today().getYear() && calendar.get(Calendar.MONTH) < CalendarDay.today().getMonth() - 1)
                    Toast.makeText(this, R.string.goal_error_date, Toast.LENGTH_SHORT).show();
                else {
                var goal = new GoalData(titleEditText.getText().toString(),
                        detailEditText.getText().toString(), DatesParser.toLocalDate(calendar.getTime()));
                goalsViewModel.insert(goal);
                finish();}
            }
        });
    }

    private void saveFABSetUp(GoalData goal) {
        saveFAB.setOnClickListener(e -> {
            if (titleEditText.getText() == null || titleEditText.getText().length() <= 0)
                Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            else {
                if (calendar.get(Calendar.YEAR) == CalendarDay.today().getYear() && calendar.get(Calendar.MONTH) < CalendarDay.today().getMonth() - 1)
                    Toast.makeText(this, R.string.goal_error_date, Toast.LENGTH_SHORT).show();
                else{
                goal.setTitle(titleEditText.getText().toString());
                goal.setDetails(detailEditText.getText().toString());
                goal.setDate(DatesParser.toLocalDate(calendar.getTime()));
                goalsViewModel.edit(goal);
                finish();
            }}
        });
    }
}
