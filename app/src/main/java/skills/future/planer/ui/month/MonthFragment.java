package skills.future.planer.ui.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import skills.future.planer.databinding.FragmentMonthBinding;
import skills.future.planer.ui.AnimateView;

public class MonthFragment extends Fragment {

    private TextView textView;
    private MaterialCalendarView materialCalendarView;
    private String date = "0"; //wybrany dzień z kalendarza
    private MonthViewModel monthViewModel;
    private FragmentMonthBinding binding;

    public MonthFragment() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);
        binding = FragmentMonthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.TEXT;
        materialCalendarView = binding.calendarView;

        // animation test
        AnimateView.scaleZoom(materialCalendarView, getContext());

        //wybór daty listener
        //test - wyświetlanie w textview
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            //NIESAMOWITE PARSOWANIE
            String parse = date.toString();
            String[] parse2 = parse.split(("\\{"));
            parse2[1] = parse2[1].substring(0, parse2[1].length() - 1);
            setDate(parse2[1]);
            textView.setText(getDate());
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}