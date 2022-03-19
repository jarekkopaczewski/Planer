package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import skills.future.planer.databinding.FragmentTaskListCreatorBinding;
import skills.future.planer.db.DBHandler;
import skills.future.planer.db.task.Priorities;
import skills.future.planer.db.task.TaskCategory;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TimePriority;


public class TaskListCreatorFragment extends Fragment {

    private FragmentTaskListCreatorBinding binding;
    private Button saveButton;
    private final Calendar myCalendar = Calendar.getInstance();
    private EditText endingDateEditText,taskTitleEditText, taskDetailsEditText;
    private CalendarDay endingCalendarDay;
    private SwitchCompat switchDate;
    public TaskListCreatorFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskListCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        switchDate = binding.SwitchDatePicker;
        switchDate.setChecked(false);
        switchDate.setOnCheckedChangeListener((compoundButton, b) ->
            endingDateEditText.setVisibility(b ? View.VISIBLE : View.INVISIBLE)
        );
        saveButton=binding.saveCreatorButton;
        saveBtnOnClickListenerSetter();
        endingDateEditText = binding.editTextDate;
        endingDateEditText.setVisibility(View.INVISIBLE);
        editTextSetter();
        taskTitleEditText= binding.EditTextTitle;
        taskDetailsEditText = binding.EditTextDetails;
        return root;
    }
    private void saveBtnOnClickListenerSetter() {
        saveButton.setOnClickListener(view1 -> {
            if(switchDate.isChecked()){
                new DBHandler(this.getContext()).addOne( new TaskData(TaskCategory.Private,
                        Priorities.Important, TimePriority.Urgent, taskTitleEditText.getText().
                        toString(),taskDetailsEditText.getText().toString(), CalendarDay.today(),
                        endingCalendarDay));
            }else {
                new DBHandler(this.getContext()).addOne( new TaskData(TaskCategory.Private,
                        Priorities.Important, TimePriority.Urgent, taskTitleEditText.getText().
                        toString(),taskDetailsEditText.getText().toString() ));
            }
            Navigation.findNavController(view1)
                    .navigate(TaskListCreatorFragmentDirections
                            .actionTaskListCreatorFragmentToNavTaskList());
        });
    }

    private void editTextSetter() {
        DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        };
        endingDateEditText.setOnClickListener(view12 ->
                new DatePickerDialog(this.getContext(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar
                        .get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel() {
//        Locale locale = new Locale("en", "UK");
//        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
//        dateFormatSymbols.setWeekdays(new String[]{
//                "Unused",
//                "Niedz",
//                "Pon",
//                "Wt",
//                "ŚR",
//                "Czw",
//                "Pt",
//                "Sob",
//        });
//
//        String pattern = "EEEEE MMMMM yyyy";
//        SimpleDateFormat simpleDateFormat =
//                new SimpleDateFormat(pattern, dateFormatSymbols);
        LocalDate date =  myCalendar.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        int day = date.getDayOfMonth(), month = date.getMonthValue(),
                year = date.getYear();
        endingCalendarDay = CalendarDay.from(year,month,day);
        endingDateEditText.setText(myCalendar.getTime().toString());
        //todo ustawić lepszy format daty
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}