package skills.future.planer.ui.summary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.summary.viewholders.SummaryViewHolder;

public class WeekSummaryTotalAdapter extends RecyclerView.Adapter<SummaryViewHolder> {

    protected final Context context;
    private final LayoutInflater layoutInflater;
    private List<SummaryData> monthSummaryList;

    public WeekSummaryTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        monthSummaryList = new ArrayList<>();
    }

    @NonNull
    protected View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SummaryViewHolder(createViewOfItem(parent, R.layout.summary_in_list_week), context);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        SummaryData current = monthSummaryList.get(position);
        holder.setEverything((SummaryData)current);
    }

    @Override
    public int getItemCount() {
        return monthSummaryList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<SummaryData> monthSummaryList) {
        this.monthSummaryList = monthSummaryList;
        notifyDataSetChanged();
    }

    public void clear(){
        this.monthSummaryList.clear();
    }
}
