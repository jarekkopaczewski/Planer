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
import skills.future.planer.ui.summary.viewholders.SummaryViewHolderWeek;

public class WeekSummaryTotalAdapter extends RecyclerView.Adapter<SummaryViewHolderWeek> {

    protected final Context context;
    private final LayoutInflater layoutInflater;
    private List<SummaryData> weekSummaryList;

    public WeekSummaryTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        weekSummaryList = new ArrayList<>();
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
    public SummaryViewHolderWeek onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SummaryViewHolderWeek(createViewOfItem(parent, R.layout.summary_in_list_week), context);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolderWeek holder, int position) {
        SummaryData current = weekSummaryList.get(position);
        holder.setEverything(current);
    }

    @Override
    public int getItemCount() {
        return weekSummaryList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<SummaryData> weekSummaryList) {
        this.weekSummaryList = weekSummaryList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addData(List<SummaryData> monthSummaryList) {
        this.weekSummaryList.addAll(monthSummaryList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.weekSummaryList.clear();
    }
}
