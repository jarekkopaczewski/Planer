package skills.future.planer.ui.day.views.summary;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.tools.DatesParser;

public class DaySummaryViewModel extends ViewModel {

    private static SummaryViewModel summaryViewModel;
    private static LifecycleOwner lifecycleOwner;
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
      summaryViewModel.getSummary2(DatesParser.toLocalDate(selectedDay), SummaryType.weekSummary)
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
