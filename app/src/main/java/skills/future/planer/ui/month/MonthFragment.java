package skills.future.planer.ui.month;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.intellij.lang.annotations.JdkConstants;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentMonthBinding;

public class MonthFragment extends Fragment {

    private TextView textView;
    private MaterialCalendarView materialCalendarView;
    private String date = "0"; //wybrany dzień z kalendarza
    private MonthViewModel monthViewModel;
    private FragmentMonthBinding binding;
    HashSet<CalendarDay> dates = new HashSet<CalendarDay>();

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
        CalendarDay d = CalendarDay.from(2022,3,27);
       // CalendarDay cd = binding.calendarView.getCurrentDate();
      //  dates.add(cd);
       // CalendarDay day = new CalendarDay(LocalDate.now().get().);

        int[] threeColors = {
                Color.rgb(0, 0, 255),
                Color.rgb(0, 255, 0),
                Color.rgb(255, 0, 0),
               // Color.rgb(255, 0, 0),

        };



    //    materialCalendarView.addDecorator(new EventDecorator(dates,threeColors));

        //wybór daty listener
        //test - wyświetlanie w textview
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            //NIESAMOWITE PARSOWANIE
            String parse = date.toString();
            dates.add(date);
            System.out.println(dates);

            String[] parse2 = parse.split(("\\{"));
            parse2[1] = parse2[1].substring(0, parse2[1].length() - 1);
            setDate(parse2[1]);
            textView.setText(getDate());
            materialCalendarView.addDecorator(new EventDecorator(dates,threeColors));
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}