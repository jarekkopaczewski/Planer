package skills.future.planer.ui.goals.creator;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import skills.future.planer.ui.month.MonthFragment;

public class GoalsCreatorActivity extends AppCompatActivity {
    private final SimpleDateFormat formatterDate =
            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
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
            var goal = goalsViewModel.findById(goalsIdToEdit.getLong("goalIdToEdit"));
            titleEditText.setText(goal.getTitle());
            detailEditText.setText(goal.getDetails());
            calendar.setTimeInMillis(goal.getDate());
            saveFABSetUp(goal);
        } else {
            calendar.set(MonthFragment.getGlobalSelectedDate().getYear(),
                    MonthFragment.getGlobalSelectedDate().getMonth() - 1,
                    MonthFragment.getGlobalSelectedDate().getDay());
            saveFABSetUp();
        }
        editTextDateGoal.setText(formatterDate.format(calendar.getTime()));
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
        DatePickerDialog.OnDateSetListener date = (datePicker, y, m, d) -> {
            calendar.set(y, m, d, 0, 0);
            editTextDateGoal.setText(formatterDate.format(calendar.getTime()));
        };

        editTextDateGoal.setOnClickListener(e -> new DatePickerDialog(this,
                date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void saveFABSetUp() {
        saveFAB.setOnClickListener(e -> {
            if (titleEditText.getText() == null || titleEditText.getText().length() <= 0)
                Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            else {
                var goal = new GoalData(titleEditText.getText().toString(),
                        detailEditText.getText().toString(), DatesParser.toLocalDate(calendar.getTime()));
                goalsViewModel.insert(goal);
                finish();
            }
        });
    }

    private void saveFABSetUp(GoalData goal) {
        saveFAB.setOnClickListener(e -> {
            if (titleEditText.getText() == null || titleEditText.getText().length() <= 0)
                Toast.makeText(this, R.string.habit_error_1, Toast.LENGTH_SHORT).show();
            else {
                goal.setTitle(titleEditText.getText().toString());
                goal.setDetails(detailEditText.getText().toString());
                goal.setDate(DatesParser.toLocalDate(calendar.getTime()));
                goalsViewModel.edit(goal);
                finish();
            }
        });
    }
}