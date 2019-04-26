package com.example.mysamstudy.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;

import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter {
    private static final String TAG = "TAG";

    private Context context;
    private ArrayList<Set> sets;
    private LayoutInflater inflater;

    public SearchListAdapter(Context context, ArrayList<Set> sets) {
        this.context = context;
        this.sets = sets;
        this.inflater = LayoutInflater.from(context);
    }

    public void replaceSet(ArrayList<Set> sets){
        this.sets = sets;
    }

    public void clearSets(){
        this.sets.clear();
    }

    @Override
    public int getCount() {
        return sets.size();
    }

    @Override
    public Set getItem(int position) {
        return sets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.snippet_base_set_list_item, parent, false);
            holder.setName = convertView.findViewById(R.id.setName);
            holder.numCards = convertView.findViewById(R.id.numCards);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.setName.setText(sets.get(position).getSetName());
        if (sets.get(position).getSetSize() != 0) {
            holder.numCards.setText(String.valueOf(sets.get(position).getSetSize()) + " Card(s)");
        }
        else {
            holder.numCards.setText("No Cards");
            holder.numCards.setTextColor(ContextCompat.getColor(context, R.color.darkOrange));
        }
        convertView.setTag(holder);

        return convertView;
    }

    public class ViewHolder{
        TextView setName, numCards;
    }
}
