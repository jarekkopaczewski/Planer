package skills.future.planer.ui.goals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.ui.AnimateView;

public class GoalTotalAdapter extends RecyclerView.Adapter<GoalViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<String> goalsList = new ArrayList<>(Arrays.asList("Awansować w pracy", "Kupić dom", "Założyć firmę"));

    public GoalTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoalViewHolder(createViewOfItem(parent, R.layout.goal_in_list), context);
    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        if (goalsList != null) {
            String current = goalsList.get(position);
            holder.setEveryThing(current);
        } else // Covers the case of data not being ready yet.
            holder.getTitle().setText("No Word");
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (goalsList != null)
            return goalsList.size();
        else return 0;
    }
}
