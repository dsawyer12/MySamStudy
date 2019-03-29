package com.example.mysamstudy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;

import java.util.ArrayList;

public class SetSelectShareAdapter extends BaseAdapter {

    Context context;
    ArrayList<Set> sets;
    LayoutInflater inflater;
    OnItemCheckedListener listener;

    public interface OnItemCheckedListener{
        void onChecked(int position);
        void onUnchecked(int position);
    }

    public SetSelectShareAdapter(Context context, ArrayList<Set> sets, OnItemCheckedListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.sets = sets;
        this.listener = listener;
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

    public int getSetId(int position){
        return sets.get(position).getSetId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.snippet_share_selection_item, parent, false);
            holder.rootView = convertView.findViewById(R.id.rootView);
            holder.set_name = convertView.findViewById(R.id.set_name);
            holder.set_size = convertView.findViewById(R.id.set_size);
            holder.checkBox = convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (sets.get(position).isShare()){
            holder.checkBox.setChecked(true);
        }

        holder.set_name.setText(sets.get(position).getSetName());
        holder.set_size.setText(String.valueOf(sets.get(position).getSetSize()) + " Card(s)");
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                    listener.onUnchecked(position);
                }
                else{
                    holder.checkBox.setChecked(true);
                    listener.onChecked(position);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder{
        LinearLayout rootView;
        TextView set_name, set_size;
        CheckBox checkBox;
    }
}