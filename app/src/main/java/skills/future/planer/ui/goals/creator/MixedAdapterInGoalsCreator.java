package skills.future.planer.ui.goals.creator;

import android.content.Context;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;

import skills.future.planer.ui.goals.pager.recycler.ICustomViewHolder;
import skills.future.planer.ui.goals.pager.recycler.MixedViewAdapter;

public class MixedAdapterInGoalsCreator extends MixedViewAdapter {
    public MixedAdapterInGoalsCreator(Context context, ComponentActivity activity) {
        super(context, activity);
    }

    @Override
    public int getItemViewType(int position) {
        return checkOrder(position);
    }

    @Override
    public int getItemCount() {
        return habitsList.size() + fullTaskList.size();
    }

    @Override
    protected int getPositionDelay(int position) {
        return position;
    }

    /**
     * Creates listener to taskView in list
     * When someone presses on a taskView it will expand or close
     */
    @Override
    protected void createListenerToExtendView(@NonNull ICustomViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {

            int positionAtomic = positionToChange.get();

            // if no window is open
            if (positionAtomic == -1) {
                positionToChange.set(holder.getBindingAdapterPosition() + 2);
                this.notifyItemChanged(holder.getBindingAdapterPosition());
            } else {
                int adapterPosition = holder.getBindingAdapterPosition();
                this.notifyItemChanged(positionAtomic);

                //if the window is open but we choose another
                if (positionAtomic != adapterPosition) {
                    positionToChange.set(adapterPosition + 2);
                    this.notifyItemChanged(positionAtomic);
                } else
                    positionToChange.set(-1);

            }
        });
    }

}
