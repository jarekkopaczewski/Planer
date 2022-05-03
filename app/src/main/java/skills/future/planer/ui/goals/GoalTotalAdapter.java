package skills.future.planer.ui.goals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Setter;
import skills.future.planer.R;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.day.MyPagerAdapter;
import skills.future.planer.ui.day.views.daylist.TaskDayAdapter;
import skills.future.planer.ui.habit.HabitBrowserFragment;
import skills.future.planer.ui.tasklist.TaskListFragment;

@Setter
public class GoalTotalAdapter extends RecyclerView.Adapter<GoalViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private Lifecycle lifecycle;
    private FragmentManager fragmentManager;
    private List<String> goalsList = new ArrayList<>(Arrays.asList("Awansować w pracy", "Awansować w pracy", "Awansować w pracy", "Awansować w pracy", "Awansować w pracy"));

    public GoalTotalAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoalViewHolder(createViewOfItem(parent, R.layout.goal_in_list), context, fragmentManager);
    }

    @NonNull
    private View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);

        MixedViewAdapter mixedViewAdapter = new MixedViewAdapter(context);
        RecyclerView list = itemView.findViewById(R.id.goalsList);
        list.setAdapter(mixedViewAdapter);
        list.setLayoutManager(new LinearLayoutManager(context));

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
