package com.example.mysamstudy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;

import java.util.ArrayList;

public class BaseSetListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Set> sets;
    private LayoutInflater layoutInflater;

    public BaseSetListAdapter(Context context, ArrayList<Set> sets) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.sets = sets;
    }

    @Override
    public int getCount() {
        return sets.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.snippet_base_set_list_item, parent, false);
            holder.set_name = convertView.findViewById(R.id.setName);
            holder.set_size = convertView.findViewById(R.id.numCards);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.set_name.setText(sets.get(position).getSetName());
        holder.set_size.setText(sets.get(position).getSetName());
        if (sets.get(position).getSetSize() == 0){
            holder.set_size.setText("No Cards");
        }
        else{
            holder.set_size.setText(sets.get(position).getSetSize());
        }
        return convertView;
    }

    public class ViewHolder {
        TextView set_name, set_size;
    }
}
