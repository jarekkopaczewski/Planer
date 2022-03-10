package skills.future.planer.ui.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import skills.future.planer.R;

class TaskTotalAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;

    private ArrayList<TaskData> taskList = new ArrayList<>();

    public TaskTotalAdapter(Context context, LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        taskList.add(new TaskData());taskList.add(new TaskData());taskList.add(new TaskData());
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

       // String currentItem = (String) getItem(position);





        return convertView;
    }
}
