package skills.future.planer.ui.tasklist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import skills.future.planer.R;
import skills.future.planer.db.AppDatabase;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.TaskDataViewModel;
import skills.future.planer.db.task.enums.category.TaskCategory;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolder;
import skills.future.planer.ui.tasklist.viewholders.TaskDataViewHolderExtended;

public class TaskTotalAdapter extends RecyclerView.Adapter<TaskDataViewHolder> implements Filterable {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<TaskData> filteredTaskList = new ArrayList<>();
    private List<TaskData> fullTaskList = new ArrayList<>();
    private List<TaskData> searchList = new ArrayList<>();
    private List<TaskData> filterList = new ArrayList<>();
    private static final int LAYOUT_SMALL = 0;
    private static final int LAYOUT_BIG = 1;
    private final AtomicInteger positionToChange = new AtomicInteger(-1);
    private final TaskDataViewModel mTaskViewModel;
    private ArrayList<TaskData> filteredItems = new ArrayList<>();
    private ArrayList<String> filters = new ArrayList<>();



    public TaskTotalAdapter(Context context, TaskDataViewModel mTaskViewModel) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mTaskViewModel = mTaskViewModel;
    }

    @NonNull
    @Override
    public TaskDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (viewType) {
            case LAYOUT_BIG -> new TaskDataViewHolderExtended(
                    createViewOfItem(parent,
                            R.layout.fragment_task_in_list_extended), context);
            default -> new TaskDataViewHolder(
                    createViewOfItem(parent,
                            R.layout.fragment_task_in_list), context);
        };
    }

    @NonNull
    protected View createViewOfItem(@NonNull ViewGroup parent, int layoutType) {
        View itemView;
        itemView = layoutInflater.inflate(layoutType, parent, false);
        AnimateView.singleAnimation(itemView, context, R.anim.scalezoom);
        return itemView;
    }

    public TaskData getTaskDataAtPosition(int position) {
        return filteredTaskList.get(position);
    }

    @SneakyThrows
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskDataViewHolder holder, int position) {
        if (filteredTaskList != null) {
            TaskData current = filteredTaskList.get(position);
            holder.setEveryThing(current);
        } else // Covers the case of data not being ready yet.
            holder.getTitle().setText("No Word");

        createListenerToExtendView(holder);
        createListenerToEditButton(holder, position);
        createListenerToTrashButton(holder, position);
        //createListenerToCheckBox(holder,position);

    }

    /**
     * Creates listener to taskView in list
     * When someone presses on a taskView it will expand or close
     */
    protected void createListenerToExtendView(@NonNull TaskDataViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {

            int positionAtomic = positionToChange.get();

            // if no window is open
            if (positionAtomic == -1) {
                positionToChange.set(holder.getAdapterPosition());
                this.notifyItemChanged(holder.getAdapterPosition());
            } else {
                int adapterPosition = holder.getAdapterPosition();
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

    /**
     * Creates listener to edit button which starts a TaskListCreatorFragment
     */
    protected void createListenerToEditButton(@NonNull TaskDataViewHolder holder, int position) {
        if (holder.itemView.findViewById(R.id.detailImageView) != null)
            holder.itemView.findViewById(R.id.detailImageView).setOnClickListener(e ->
                    Navigation.findNavController(holder.itemView)
                            .navigate(TaskListFragmentDirections
                                    .navToEditTaskListCreatorFragment(fullTaskList.get(position).getTaskDataId())));
    }

    /**
     * Creates listener to edit button which starts a TaskListCreatorFragment
     */
    protected void createListenerToTrashButton(@NonNull TaskDataViewHolder holder, int position) {
        if (holder.itemView.findViewById(R.id.trashImageView) != null)
            holder.itemView.findViewById(R.id.trashImageView).setOnClickListener(e -> {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.removetask);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        var task = fullTaskList.get(position);
                        mTaskViewModel.deleteTaskData(task);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                holder.itemView.startAnimation(animation);
            });
    }

    @SuppressLint({"NotifyDataSetChanged"})
    private void createListenerToCheckBox(@NonNull TaskDataViewHolder holder, int position) {
        if (holder.itemView.findViewById(R.id.checkBoxTask) != null){
            var task = fullTaskList.get(position);
            holder.getCheckBox().setChecked(task.getStatus());
            holder.itemView.findViewById(R.id.checkBoxTask).setOnClickListener(view -> {
                task.setStatus(holder.getCheckBox().isChecked());
                var taskDataDao = AppDatabase.getInstance(context).taskDataTabDao();
                taskDataDao.editOne(task);
                try {
                    CategoryFilter(filters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
                //notifyItemChanged(position);
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == positionToChange.get())
            return LAYOUT_BIG;
        return LAYOUT_SMALL;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredTaskList(List<TaskData> words) {
        filteredTaskList = words;
        fullTaskList = new ArrayList<>(filteredTaskList);
        notifyDataSetChanged();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (filteredTaskList != null)
            return filteredTaskList.size();
        else return 0;
    }

    public List<TaskData> getFullTaskList() {
        return fullTaskList;
    }

    /**
     * Filter for task's category
     *
     * @param filters list of checked filters names
     * @throws Exception exception
     */
    @SuppressLint("NotifyDataSetChanged")
    public void CategoryFilter(ArrayList<String> filters) throws Exception {

        TaskCategory category = null;
        Priorities priorities = null;
        TimePriority timePriority = null;
        int status = -1;
        //this.filters=filters;


        //checking which filter is checked
        for (String filter : filters) {
            if (filter.equals(TaskCategory.Private.toString())) {
                category = TaskCategory.Private;
            }
            if (filter.equals(TaskCategory.Work.toString())) {
                category = TaskCategory.Work;
            }
            if (filter.equals(TimePriority.Urgent.toString())) {
                timePriority = TimePriority.Urgent;
            }
            if (filter.equals(TimePriority.NotUrgent.toString())) {
                timePriority = TimePriority.NotUrgent;
            }
            if (filter.equals(Priorities.Important.toString())) {
                priorities = Priorities.Important;
            }
            if (filter.equals(Priorities.NotImportant.toString())) {
                priorities = Priorities.NotImportant;
            }
            if (filter.equals("NotDone")) {
                status = 0;
            }
            if (filter.equals("Done")) {
                status = 1;
            }
        }

        //list of filtered tasks
        List<TaskData> list = new ArrayList<>();
        List<TaskData> list2;


        //checks which query to execute
        if (category == null && priorities == null && timePriority != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(timePriority);
        }
        if (category == null && timePriority == null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities);
        }
        if (timePriority == null && priorities == null && category != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(category);
        }
        if (category == null && timePriority != null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities, timePriority);
        }
        if (timePriority == null && category != null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities, category);
        }
        if (priorities == null && timePriority != null && category != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(category, timePriority);
        }
        if (category != null && timePriority != null && priorities != null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(priorities, timePriority, category);
        }
        if (category == null && priorities == null && timePriority == null) {
            list = AppDatabase.getInstance(context).taskDataTabDao().getTaskData2();
        }

        //checks if filter by status
        if (status != -1) {
            list2 = AppDatabase.getInstance(context).taskDataTabDao().getTaskData(status);
            ArrayList list3 = (ArrayList) list.stream()
                    .distinct()
                    .filter(list2::contains)
                    .collect(Collectors.toList());

            filterList=list3;
        } else {
            filterList=list;
        }

        notifyDataSetChanged();
    }

    /**
     * Creates new filter
     *
     * @return new Task filter
     */
    @Override
    public Filter getFilter() {
        return new TaskFilter();
    }

    //todo Trzeba zrobić synchronizacje po filtrze jezeli będą zmiany


    /**
     * Filter class
     * Searches for string match in task titles
     */
    private class TaskFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           // CategoryFilter(filters);
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {

                for (int i = 0, l = filterList.size(); i < l; i++) {
                    TaskData taskData = filterList.get(i);
                    if (taskData.getTaskTitleText().toLowerCase().contains(constraint))
                        filteredItems.add(taskData);
                }
            }
            return result;
        }

        @SuppressLint("NotifyDataSetChanged")
        @SuppressWarnings("unchecked")
        @Override
        public void publishResults(CharSequence constraint, FilterResults results) {

            filteredTaskList.clear();
            if(constraint.toString().length() > 0) {
                filteredTaskList.addAll((ArrayList<TaskData>) filteredItems);
                filteredItems.clear();
            }else{
                filteredTaskList.addAll((ArrayList<TaskData>) filterList);
            }
            notifyDataSetChanged();
        }



}}
