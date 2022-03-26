package skills.future.planer.ui.tasklist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.database.TaskDataTable;
import skills.future.planer.db.task.enums.priority.Priorities;
import skills.future.planer.db.task.enums.priority.TimePriority;
import skills.future.planer.ui.AnimateView;

class TaskTotalAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;

    private List<TaskData> taskList = new ArrayList<>();

    public TaskTotalAdapter(Context context, LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        refreshTaskList();
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.fragment_task_in_list, parent, false);

        TaskData currentTask = (TaskData) getItem(position);

        setTextTitle(currentTask, convertView);
        setIconCategory(currentTask, convertView);
        setColor(currentTask, convertView);

        // animation test
        View finalConvertView = convertView;
        convertView.setOnClickListener(e-> AnimateView.animateInOut(finalConvertView, context));
        // animation test

        AnimateView.scaleZoom(finalConvertView, context);

        return convertView;
    }

    /**
     * Sets color of left card view
     * @param taskData current task
     * @param convertView current task view
     */
    public void setColor(TaskData taskData, View convertView)
    {
        System.out.printf(taskData.getPriorities().toString() + " " + taskData.getTimePriority().toString());
        // urgent & important
        if( taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.Important)
            ((CardView) convertView.findViewById(R.id.colorMarkCardView)).setCardBackgroundColor(Color.rgb(255, 102, 109));
        // urgent & not important
        else if( taskData.getTimePriority() == TimePriority.Urgent && taskData.getPriorities() == Priorities.NotImportant)
            ((CardView) convertView.findViewById(R.id.colorMarkCardView)).setCardBackgroundColor(Color.rgb(95, 191, 217));
        // not urgent & important
        else if( taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.Important)
            ((CardView) convertView.findViewById(R.id.colorMarkCardView)).setCardBackgroundColor(Color.rgb(245, 189, 76));
        // not urgent & not important
        else if( taskData.getTimePriority() == TimePriority.NotUrgent && taskData.getPriorities() == Priorities.NotImportant)
            ((CardView) convertView.findViewById(R.id.colorMarkCardView)).setCardBackgroundColor(Color.rgb(255, 153, 193));
    }

    /**
     * Gets data from database
     */
    public void refreshTaskList() {
        try {
            taskList = new TaskDataTable(context).getTaskData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets title of tasks
     */
    private void setTextTitle(TaskData task, View convertView) {
        String titleText = task.getTaskTitleText();
        String dateText =  setDateTextView(task);
        ((TextView) convertView.findViewById(R.id.taskTitleTextView)).setText(titleText);
        ((TextView) convertView.findViewById(R.id.taskDateTextView)).setText(dateText);
    }

    /**
     * Merges date strings
     */
    // delete "/n"
    private String setDateTextView(TaskData task) {
        String dateView = "";
        if (task.getEndingDate() != null) {
            if (task.getStartingDate() != null)
                dateView = convertCalendarDay(task.getStartingDate()) + " - ";
            dateView += convertCalendarDay(task.getEndingDate());
        }
        return dateView;
    }

    /**
     * Coverts calendar day format to string
     *
     * @param calendarDay date in CalendarDay format
     * @return CalendarDay in string format
     */
    private String convertCalendarDay(CalendarDay calendarDay) {
        return calendarDay.getDay() + "." + calendarDay.getMonth() + "." + calendarDay.getYear();
    }

    /**
     * Sets icon of category
     */
    private void setIconCategory(TaskData task, View convertView) {
        if (task.getCategory() != null) {
            switch (task.getCategory()) {
                case Work -> ((ImageView) convertView.findViewById(R.id.iconTaskCategory))
                        .setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.home,
                                null));
                case Private -> ((ImageView) convertView.findViewById(R.id.iconTaskCategory))
                        .setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.briefcase,
                                null));
            }
        }
    }
}
