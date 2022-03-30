package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.concurrent.FutureTask;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentTaskListCreatorBinding;
import skills.future.planer.db.AppDatabase;
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

        processFabColor();

        switchPriorities.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchPriorities.isChecked()) binding.imageViewImportant.setImageResource(R.drawable.trash);
            else binding.imageViewImportant.setImageResource(R.drawable.star);
            processFabColor();
        });

        switchTimePriorities.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchTimePriorities.isChecked()) binding.imageViewTaskUrgent.setImageResource(R.drawable.ice);
            else binding.imageViewTaskUrgent.setImageResource(R.drawable.fire);
            processFabColor();
        });

        switchCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchCategory.isChecked()) binding.imageViewTaskDetails2.setImageResource(R.drawable.case_doodle);
            else binding.imageViewTaskDetails2.setImageResource(R.drawable.home_doodle);
            processFabColor();
        });

        return root;
    }

    /**
     * Changes color of fab button
     */
    private void processFabColor()
    {
        AnimateView.singleAnimation(saveButton, getContext(), R.anim.rotate);

        if( !switchPriorities.isChecked()  && !switchTimePriorities.isChecked() )
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.RED.getColor()));
        else if( switchPriorities.isChecked()  && !switchTimePriorities.isChecked() )
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.BLUE.getColor()));
        else if( !switchPriorities.isChecked()  && switchTimePriorities.isChecked() )
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.YELLOW.getColor()));
        else if( switchPriorities.isChecked()  && switchTimePriorities.isChecked() )
            saveButton.setBackgroundTintList(ColorStateList.valueOf(Colors.PINK.getColor()));
    }

    /**
     * Method is saveBtn On Click Listener
     * Listener will add new TaskData to database and back to listView
     */
    private void saveBtnOnClickListenerSetter() {
        saveButton.setOnClickListener(view1 -> {
            AnimateView.animateInOut(saveButton, getContext());

            if(taskTitleEditText.getText().toString().isEmpty())
            {
                Toast toast = Toast.makeText(this.getContext(),
                        "Tytuł nie może być pusty!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                taskDetailsEditText.getText();
                TaskData data = new TaskData(
                        switchCategory.isChecked() ? TaskCategory.Private : TaskCategory.Work,
                        switchPriorities.isChecked() ? Priorities.NotImportant : Priorities.Important,
                        switchTimePriorities.isChecked() ? TimePriority.NotUrgent : TimePriority.Urgent,
                        taskTitleEditText.getText().toString(),
                        taskDetailsEditText.getText().toString());
                if (switchDate.isChecked()) {
                    //if user want to add dates
                    data.setEndingCalendarDate(endingCalendarDay);
                    data.setStartingCalendarDate(beginCalendarDay);
                }
                Object result = new Object();
                FutureTask<Object> futureTask = new FutureTask<>(() -> {
                    AppDatabase.getInstance(this.getContext().getApplicationContext()).taskDataTabDao().addOne(data);
                    data.setTaskDataId(AppDatabase.getInstance(this.getContext().getApplicationContext()).taskDataTabDao().getIdOfLastAddedTask());
                }, result);
                futureTask.run();
                Navigation.findNavController(view1)
                        .navigate(TaskListCreatorFragmentDirections
                                .actionTaskListCreatorFragmentToNavTaskList());
            }
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