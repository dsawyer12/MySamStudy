package com.example.mysamstudy.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.main.SetActivity;
import com.example.mysamstudy.main.SetStartActivity;
import com.example.mysamstudy.objects.Set;

import java.util.ArrayList;

public class CardListAdapter extends BaseAdapter {
    private static final String TAG = "TAG";
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Set> sets;
    private ArrayList<Set> delete_set;
    private boolean is_deleteView = false;
    private OnItemLongClickListener listener;

    public interface OnItemLongClickListener{
        void onLongClick(boolean in_deleteView);
        void onClick();
        void onClick(Set set);
    }

    public CardListAdapter(Context context, ArrayList<Set> sets, OnItemLongClickListener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.sets = sets;
        this.listener = listener;
        delete_set = new ArrayList<>();
    }

    public ArrayList<Set> getDelete_set(){
        return delete_set;
    }

    public void setIs_deleteView(boolean is_deleteView){
        this.is_deleteView = is_deleteView;
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
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.snippet_set_list_item, parent, false);
            holder.rootView = convertView.findViewById(R.id.rootView);
            holder.start = convertView.findViewById(R.id.start);
            holder.checkBox = convertView.findViewById(R.id.checkbox);
            holder.setName = convertView.findViewById(R.id.setName);
            holder.numCards = convertView.findViewById(R.id.numCards);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setName.setText(sets.get(position).getSetName());
        if (sets.get(position).getSetSize() != 0){
            holder.numCards.setText(String.valueOf(sets.get(position).getSetSize() + " card"));
            holder.start.setEnabled(true);
        }
        else{
            holder.numCards.setText("No Cards");
            holder.numCards.setTextColor(ContextCompat.getColor(context, R.color.darkOrange));
            holder.start.setEnabled(false);
        }

        if (is_deleteView){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.start.setVisibility(View.GONE);
        }
        else{
            holder.checkBox.setVisibility(View.GONE);
            holder.start.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_deleteView){
                    if (holder.checkBox.isChecked()){
                        holder.checkBox.setChecked(false);
                        delete_set.remove(sets.get(position));
                        listener.onClick();
                    }
                    else{
                        holder.checkBox.setChecked(true);
                        delete_set.add(sets.get(position));
                        listener.onClick();
                    }
                }
                else{
                    listener.onClick(sets.get(position));
                }
            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (is_deleteView){
                    is_deleteView = false;
                    listener.onLongClick(is_deleteView);
                }
                else{
                    is_deleteView = true;
                    listener.onLongClick(is_deleteView);
                    holder.checkBox.setChecked(true);
                    delete_set.add(sets.get(position));
                }
                return true;
            }
        });

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SetStartActivity.class);
                intent.putExtra("mySet", sets.get(position));
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    private class ViewHolder{
        RelativeLayout rootView;
        TextView setName, numCards;
        Button start;
        CheckBox checkBox;
    }
}
