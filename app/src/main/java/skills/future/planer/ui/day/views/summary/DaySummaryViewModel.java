package skills.future.planer.ui.day.views.summary;

import android.annotation.SuppressLint;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.DayOfWeek;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;

import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.tools.DatesParser;

public class DaySummaryViewModel extends ViewModel {

    private static SummaryViewModel summaryViewModel;
    private static LifecycleOwner lifecycleOwner;
    @SuppressLint("StaticFieldLeak")
    private static DaySummaryAdapter daySummaryAdapter;

    public static void setSummaryViewModel(SummaryViewModel summaryViewModel) {
        DaySummaryViewModel.summaryViewModel = summaryViewModel;
    }

    public static void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        DaySummaryViewModel.lifecycleOwner = lifecycleOwner;
    }

    public static void setDaySummaryAdapter(DaySummaryAdapter daySummaryAdapter) {
        DaySummaryViewModel.daySummaryAdapter = daySummaryAdapter;
    }

    public void updateDate(CalendarDay selectedDay) {

        LocalDate day = DatesParser.toLocalDate(selectedDay);

        while(selectedDay.getDate().getDayOfWeek() != DayOfWeek.MONDAY){
                LocalDate date = DatesParser.toLocalDate(selectedDay).minusDays(1);
                selectedDay = DatesParser.toCalendarDay(date);
        }

        int weekNumber = DatesParser.toLocalDate(selectedDay).get(ChronoField.ALIGNED_WEEK_OF_YEAR) + 1;

      summaryViewModel.getSummary2(day, weekNumber)
              .observe(lifecycleOwner, summary -> daySummaryAdapter.setSummaryDataList(summary));
    }

    public static SummaryViewModel getSummaryViewModel() {
        return summaryViewModel;
    }

    public static LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    public static DaySummaryAdapter getDaySummaryAdapter() {
        return daySummaryAdapter;
    }

}
