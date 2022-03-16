package skills.future.planer.ui.tasklist;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.Calendar;

import skills.future.planer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskListCreatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListCreatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button saveButton;
    private final Calendar myCalendar = Calendar.getInstance();
    private EditText editText;
    private Switch switchDate;

    public TaskListCreatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskListCreatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskListCreatorFragment newInstance(String param1, String param2) {
        TaskListCreatorFragment fragment = new TaskListCreatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list_creator, container, false);

        switchDate = view.findViewById(R.id.SwitchDatePicker);
        switchDate.setChecked(false);
        switchDate.setOnCheckedChangeListener((compoundButton, b) -> {
            editText.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        });
        saveButton = view.findViewById(R.id.saveCreatorButton);
        saveBtnOnClickListenerSetter();
        editText = (EditText) view.findViewById(R.id.editTextDate);
        editTextSetter();

        return view;
    }

    private void saveBtnOnClickListenerSetter() {
        saveButton.setOnClickListener(view1 -> {
            Navigation.findNavController(view1)
                    .navigate(TaskListCreatorFragmentDirections.actionTaskListCreatorFragmentToNavTaskList());
        });
    }

    private void editTextSetter() {
        DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        };
        editText.setOnClickListener(view12 -> new DatePickerDialog(this.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
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
        editText.setText(myCalendar.getTime().toString());
        //todo ustawić lepszy format daty
    }
}