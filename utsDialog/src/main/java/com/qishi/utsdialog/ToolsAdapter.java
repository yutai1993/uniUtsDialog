package com.qishi.utsdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ToolsAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> toolsList;

    public ToolsAdapter(Context context, List<Map<String, Object>> toolsList) {
        this.context = context;
        this.toolsList = toolsList;
    }

    @Override
    public int getCount() {
        return toolsList.size();
    }

    @Override
    public Object getItem(int position) {
        return toolsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, Object> item = toolsList.get(position);
        holder.textView.setText(String.valueOf(item.get("value")));
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
