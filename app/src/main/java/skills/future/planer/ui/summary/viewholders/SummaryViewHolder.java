package skills.future.planer.ui.summary.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryType;

@Getter
public class SummaryViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    protected Context context;

    public SummaryViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.summaryNameText);
        this.context = context;
    }

    public void setEverything(SummaryData summaryData)
    {
        if( summaryData.getSummaryType() == SummaryType.monthSummary )
            nameTextView.setText(context.getResources().getStringArray(R.array.months)[summaryData.getMonth()-1]);
        else if( summaryData.getSummaryType() == SummaryType.weekSummary )
            nameTextView.setText("Tydzień nr " + summaryData.getWeekNumber());
    }
}
