package skills.future.planer.ui.summary.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.habit.HabitCreatorActivity;
import skills.future.planer.ui.summary.SummaryEditorActivity;

@Getter
public class SummaryViewHolderWeek extends SummaryViewHolder  {
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private CardView cardViewSummaryWeek;

    public SummaryViewHolderWeek(@NonNull View itemView, Context context) {
        super(itemView, context);
        this.cardViewSummaryWeek = itemView.findViewById(R.id.cardViewSummaryWeek);
    }

    @SuppressLint({"SetTextI18n"})
    public void setEverything(SummaryData summaryData)
    {
        Calendar date = Calendar.getInstance();
        date.set(summaryData.getYear(), summaryData.getMonth(), 1);
        date.set(Calendar.WEEK_OF_YEAR, summaryData.getWeekNumber());
        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Calendar date2 = Calendar.getInstance();
        date2.set(summaryData.getYear(), summaryData.getMonth(), 1);
        date2.set(Calendar.WEEK_OF_YEAR, summaryData.getWeekNumber());
        date2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        if( summaryData.getSummaryType() == SummaryType.monthSummary ){
            var text = context.getResources().getStringArray(R.array.months)[summaryData.getMonth()-1];
            nameTextView.setText("Podsumowanie " + text);
            cardViewSummaryWeek.setCardBackgroundColor(context.getColor(R.color.month_summary));

        }
        else if( summaryData.getSummaryType() == SummaryType.weekSummary ){
            nameTextView.setText("Tydzień od " + formatterDate.format(date.getTime()) + " do " + formatterDate.format(date2.getTime()));
        }

        if(detailImageView != null)
            detailImageView.setOnClickListener(e->{
                AnimateView.animateInOut(detailImageView, context);
                var intent = new Intent(this.getContext(), SummaryEditorActivity.class);
                var bundle = new Bundle();
                bundle.putInt("summaryId", summaryData.getSummaryId().intValue());
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
    }
}
