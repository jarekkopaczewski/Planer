package skills.future.planer.ui.summary.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;

import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.summary.adapter.WeekSummaryTotalAdapter;

public class SummaryViewHolderExtended extends SummaryViewHolder {

    private WeekSummaryTotalAdapter weekSummaryTotalAdapter;
    private final ImageView detailImageView2;
    private final RecyclerView weekRecyclerView;
    private final Fragment fragment;

    @SuppressLint("ResourceAsColor")
    public SummaryViewHolderExtended(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView, context);
        this.weekRecyclerView = itemView.findViewById(R.id.weekRecyclerView);
        this.detailImageView2 = itemView.findViewById(R.id.detailImageView2);
        this.fragment = fragment;
    }

    @Override
    public void setEverything(SummaryData summaryData) {
        super.setEverything(summaryData);
        SummaryViewModel summaryViewModel = new ViewModelProvider(fragment).get(SummaryViewModel.class);
        weekSummaryTotalAdapter = new WeekSummaryTotalAdapter(context);
        weekRecyclerView.setAdapter(weekSummaryTotalAdapter);
        weekRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));

        try {
            CalendarDay calendarDay = CalendarDay.today();
            LocalDate localDate = DatesParser.toLocalDate(calendarDay);

            weekSummaryTotalAdapter.setData(summaryViewModel.getWeeksFromMonthSummary(summaryData.getYear(), summaryData.getMonth()));

            if(localDate.getMonthValue() > summaryData.getMonth())
                weekSummaryTotalAdapter.addData(summaryViewModel.getMonthSummary(summaryData.getYear(), summaryData.getMonth()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (detailImageView2 != null)
            detailImageView2.setOnClickListener(e -> {
                AnimateView.singleAnimation(detailImageView2, context, R.anim.rotate2);
                itemView.callOnClick();
            });
    }
}
