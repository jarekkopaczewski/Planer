package skills.future.planer.ui.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import skills.future.planer.R;

class TaskTotalAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    private ArrayList<String> taskListString = new ArrayList<> (Arrays.asList("Pierwsze zadanie", "Drugie zadanie", "Trzecie zadanie"));

    public TaskTotalAdapter(Context context, LayoutInflater layoutInflater){
        this.layoutInflater = layoutInflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return taskListString.size();
    }

    @Override
    public Object getItem(int position) {
        return taskListString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.activity_listview, parent, false);
        }
        String currentItem = (String) getItem(position);

        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.textViewList);

        textViewItemName.setText(currentItem);

        return convertView;
    }
}
