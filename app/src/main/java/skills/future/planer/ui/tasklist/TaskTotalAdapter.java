package skills.future.planer.ui.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.db.DBHandler;
import skills.future.planer.db.task.TaskData;

class TaskTotalAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;

    private List<TaskData> taskList = new ArrayList<>();

    public TaskTotalAdapter(Context context, LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        refreshTaskList();
    }

    public void refreshTaskList(){
        try {
            taskList = new DBHandler(context).getTaskData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            ((TextView) convertView.findViewById(R.id.taskTitleTextView)).setText(dateView);
        } else
            (convertView.findViewById(R.id.taskTitleTextView)).setVisibility(View.INVISIBLE);
    }

    private void setIconCategory(TaskData task, View convertView) {
        if (task.getCategory() != null) {
            switch (task.getCategory()){
                case Work:
                    ((ImageView) convertView.findViewById(R.id.iconTaskCategory))
                            .setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                    R.drawable.home,
                                    null));
                    break;
                case Private:
                    ((ImageView) convertView.findViewById(R.id.iconTaskCategory))
                            .setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                    R.drawable.briefcase,
                                    null));
                    break;
            }
        }
    }
}
