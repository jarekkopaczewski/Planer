package skills.future.planer.ui.day.views.summary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.views.matrix.MatrixListTotalAdapter;

public class DaySummaryAdapter extends RecyclerView.Adapter<DaySummaryAdapter.DaySummaryViewHolder> {

    private List<SummaryData> summaryDataList = new ArrayList<>();
    private Context context;
    private SummaryViewModel summaryViewModel;
    private DaySummaryViewModel daySummaryViewModel;
    private final LayoutInflater layoutInflater;

    public DaySummaryAdapter(Context context, SummaryViewModel summaryViewModel, DaySummaryViewModel daySummaryViewModel) {
        this.context = context;
        this.summaryViewModel = summaryViewModel;
        this.daySummaryViewModel = daySummaryViewModel;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public List<SummaryData> getSummaryDataList() {
        return summaryDataList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSummaryDataList(List<SummaryData> summaryDataList) {
        if(this.summaryDataList.size() != 0) {
            if (summaryDataList.get(0).getWeekNumber() != this.summaryDataList.get(0).getWeekNumber()){
                this.summaryDataList = summaryDataList;
                notifyDataSetChanged();
            }
        }else {
            this.summaryDataList = summaryDataList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public DaySummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View itemView = layoutInflater.inflate(R.layout.fragment_summary, parent, false);
        return new DaySummaryViewHolder(createViewOfItem(parent,R.layout.summary_day));
    }

    @NonNull
    protected View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView = layoutInflater.inflate(layoutType, parent, false);
        return itemView;
    }

    @Override
    public void onBindViewHolder(@NonNull DaySummaryViewHolder holder, int position) {
        if(summaryDataList != null){
            holder.setEverything(summaryDataList.get(0));
        }else {
            holder.text1.setText("");
            holder.text2.setText("");
            holder.text3.setText("");
        }
    }

    @Override
    public int getItemCount() {
        if (summaryDataList != null)
             return summaryDataList.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    class DaySummaryViewHolder extends RecyclerView.ViewHolder{

        private final TextView text1, text2, text3;

        public DaySummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.summaryText1);
            text2 = itemView.findViewById(R.id.summaryText2);
            text3 = itemView.findViewById(R.id.summaryText3);
        }

        public void setEverything(SummaryData summaryData){
            text1.setText(summaryData.getAchievements());
            text2.setText(summaryData.getNotFinished());
            text3.setText(summaryData.getToWorkOut());
            System.out.println(text1);
            System.out.println(text2);
            System.out.println(text3);
        }
    }
}
