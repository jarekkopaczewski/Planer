package skills.future.planer.ui.day.views.summary;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.db.summary.SummaryData;

public class DaySummaryAdapter extends RecyclerView.Adapter {

    private List<SummaryData> summaryDataList = new ArrayList<>();

    public List<SummaryData> getSummaryDataList() {
        return summaryDataList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSummaryDataList(List<SummaryData> summaryDataList) {
        this.summaryDataList = summaryDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
