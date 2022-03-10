package skills.future.planer.ui.tasklist;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import skills.future.planer.R;

class TaskTotalAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;

    private ArrayList<TaskData> taskList = new ArrayList<>();

    public TaskTotalAdapter(Context context, LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        taskList.add(new TaskData(context, 0));
        taskList.add(new TaskData(context, 1));
        taskList.add(new TaskData(context, 0));
        taskList.add(new TaskData(context, 0));
        taskList.add(new TaskData(context, 1));
        taskList.add(new TaskData(context, 0));
        taskList.add(new TaskData(context, 1));
        taskList.add(new TaskData(context, 0));

    }

    public void addItemToList(TaskData task) {
        taskList.add(task);
    }

    public void deleteItemFromList(TaskData task) {
        taskList.remove(task);
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

        setDateTextView(currentTask, convertView);
        setIconCategory(currentTask, convertView);

        return convertView;
    }

    private void setDateTextView(TaskData task, View convertView) {
        String dateView = "";
        if (task.getEndingDate() != null) {
            if (task.getStartingDate() != null)
                dateView = task.getStartingDate().toString() + " ";
            dateView += task.getEndingDate();
            ((TextView) convertView.findViewById(R.id.taskDescriptionTextView)).setText(dateView);
        } else
            (convertView.findViewById(R.id.taskDescriptionTextView)).setVisibility(View.INVISIBLE);
    }

    private void setIconCategory(TaskData task, View convertView) {
        if (task.getCategory() != null)
            ((ImageView) convertView.findViewById(R.id.iconTaskCategory))
                    .setImageDrawable(task.getCategory());
    }


}
