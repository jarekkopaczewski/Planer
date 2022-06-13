package skills.future.planer.ui.summary.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.ui.AnimateView;

@Getter
public class SummaryViewHolder extends RecyclerView.ViewHolder {
    protected TextView nameTextView;
    protected ImageView detailImageView;
    protected Context context;

    public SummaryViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.summaryNameText);
        try {
            this.detailImageView = itemView.findViewById(R.id.detailImageView3);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    public void setEverything(SummaryData summaryData) {
        if (summaryData.getSummaryType() == SummaryType.monthSummary) {
            var text = context.getResources().getStringArray(R.array.months)[summaryData.getMonth() - 1];
            String upper = text.substring(0, 1).toUpperCase() + text.substring(1);
            nameTextView.setText(upper);
        }

        if (detailImageView != null)
            detailImageView.setOnClickListener(e -> {
                AnimateView.singleAnimation(detailImageView, context, R.anim.rotate2);
                itemView.callOnClick();
            });
    }
}
