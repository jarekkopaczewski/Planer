package skills.future.planer.ui.summary.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.summary.SummaryEditorActivity;

@Getter
public class SummaryViewHolderWeek extends SummaryViewHolder {
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private final CardView cardViewSummaryWeek;
    private final ImageView detailImageViewWeek;

    public SummaryViewHolderWeek(@NonNull View itemView, Context context) {
        super(itemView, context);
        this.cardViewSummaryWeek = itemView.findViewById(R.id.cardViewSummaryWeek);
        this.detailImageViewWeek = itemView.findViewById(R.id.detailImageViewWeek);
    }

    @SuppressLint({"SetTextI18n"})
    public void setEverything(SummaryData summaryData) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, summaryData.getYear());
        date.setFirstDayOfWeek(Calendar.MONDAY);
        date.setMinimalDaysInFirstWeek(7);
        date.set(Calendar.WEEK_OF_YEAR, summaryData.getWeekNumber());

        System.out.println("====================");
        System.out.println("====================");
        System.out.println(summaryData.getWeekNumber());
        System.out.println(date.get(Calendar.DAY_OF_WEEK));
        System.out.println(date.get(Calendar.DAY_OF_MONTH));
        System.out.println(date.get(Calendar.MONTH));

        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);


        Calendar date2 = Calendar.getInstance();
        date2.set(Calendar.YEAR, summaryData.getYear());
        date2.setMinimalDaysInFirstWeek(7);
        date2.setFirstDayOfWeek(Calendar.MONDAY);

        date2.set(Calendar.WEEK_OF_YEAR, summaryData.getWeekNumber());

        System.out.println("====================");
        System.out.println(date2.get(Calendar.DAY_OF_WEEK));
        System.out.println(date2.get(Calendar.DAY_OF_MONTH));
        System.out.println(date2.get(Calendar.MONTH));
        System.out.println("====================");
        System.out.println("====================");

        date2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        if (summaryData.getSummaryType() == SummaryType.monthSummary) {
            var text = context.getResources().getStringArray(R.array.months)[summaryData.getMonth() - 1];
            nameTextView.setText("Podsumowanie " + text);
            cardViewSummaryWeek.setCardBackgroundColor(context.getColor(R.color.month_summary));

        } else if (summaryData.getSummaryType() == SummaryType.weekSummary) {
            nameTextView.setText("Tydzień od " + formatterDate.format(date.getTime()) + " do " + formatterDate.format(date2.getTime()));
        }

        if (detailImageViewWeek != null)
            detailImageViewWeek.setOnClickListener(e -> {
                AnimateView.animateInOut(detailImageViewWeek, context);
                var intent = new Intent(this.getContext(), SummaryEditorActivity.class);
                var bundle = new Bundle();
                bundle.putLong("summaryId", summaryData.getSummaryId());
                bundle.putBoolean("editable", true);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });

        itemView.setOnClickListener(e -> {
            AnimateView.animateInOut(itemView, context);
            var intent = new Intent(this.getContext(), SummaryEditorActivity.class);
            var bundle = new Bundle();
            bundle.putLong("summaryId", summaryData.getSummaryId());
            bundle.putBoolean("editable", false);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }
}
