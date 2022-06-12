package skills.future.planer.ui.summary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import skills.future.planer.R;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.summary.viewholders.SummaryViewHolder;
import skills.future.planer.ui.summary.viewholders.SummaryViewHolderExtended;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolderExtended;

public class SummaryTotalAdapter extends RecyclerView.Adapter<SummaryViewHolder> {

    protected final Context context;
    private final LayoutInflater layoutInflater;
    private List<SummaryData> monthSummaryList;
    private Fragment fragment;
    private static final int LAYOUT_SMALL = 0;
    private static final int LAYOUT_BIG = 1;
    private final AtomicInteger positionToChange = new AtomicInteger(-1);

    public SummaryTotalAdapter(Context context, Fragment fragment) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
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
        return switch (viewType) {
            case LAYOUT_BIG -> new SummaryViewHolderExtended(createViewOfItem(parent, R.layout.summary_in_list_extended), context, fragment);
            default -> new SummaryViewHolder(createViewOfItem(parent, R.layout.summary_in_list), context);
        };
    }

    /**
     * Creates listener to taskView in list
     * When someone presses on a taskView it will expand or close
     */
    protected void createListenerToExtendView(@NonNull SummaryViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {

            int positionAtomic = positionToChange.get();

            // if no window is open
            if (positionAtomic == -1) {
                positionToChange.set(holder.getBindingAdapterPosition());
                this.notifyItemChanged(holder.getBindingAdapterPosition());
            } else {
                int adapterPosition = holder.getBindingAdapterPosition();
                this.notifyItemChanged(positionAtomic);

                //if the window is open but we choose another
                if (positionAtomic != adapterPosition) {
                    positionToChange.set(adapterPosition);
                    this.notifyItemChanged(positionAtomic);
                } else
                    positionToChange.set(-1);

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == positionToChange.get())
            return LAYOUT_BIG;
        return LAYOUT_SMALL;
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        SummaryData current = monthSummaryList.get(position);
        holder.setEverything((SummaryData)current);
        createListenerToExtendView(holder);
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
