package com.example.mysamstudy.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.main.SetStartActivity;
import com.example.mysamstudy.objects.Set;

import java.util.ArrayList;

public class SetListRecyclerView extends RecyclerView.Adapter<SetListRecyclerView.ViewHolder> {
    private static final String TAG = "TAG";

    private Context context;
    private ArrayList<Set> sets;
    private ArrayList<Set> delete_set;
    private boolean delete_view = false;

    public interface OnItemClickListener{
        void onSetClick(Set set);
        void onSetLongClick(boolean delete_view);
    }
    OnItemClickListener listener;

    public SetListRecyclerView(Context context, ArrayList<Set> sets, OnItemClickListener listener) {
        this.context = context;
        this.sets = sets;
        this.listener = listener;
        delete_set = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rootView;
        TextView setName, numCards;
        Button start;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
            setName = itemView.findViewById(R.id.setName);
            numCards = itemView.findViewById(R.id.numCards);
            start = itemView.findViewById(R.id.start);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public ArrayList<Set> getDelete_set(){
        return delete_set;
    }

    public void setIs_deleteView(boolean delete_view){
        this.delete_view = delete_view;
        delete_set.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_set_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
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

        if (delete_view){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.start.setVisibility(View.GONE);
            if (sets.get(holder.getAdapterPosition()).isSelected())
                holder.checkBox.setChecked(true);
            else
                holder.checkBox.setChecked(false);
        }
        else{
            delete_set.clear();
            holder.checkBox.setVisibility(View.GONE);
            holder.start.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete_view){
                    if (holder.checkBox.isChecked()){
                        holder.checkBox.setChecked(false);
                        delete_set.remove(sets.get(holder.getAdapterPosition()));
                    }
                    else{
                        holder.checkBox.setChecked(true);
                        delete_set.add(sets.get(holder.getAdapterPosition()));
                    }
                }
                else{
                    listener.onSetClick(sets.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (delete_view){
                    delete_view = false;
                    listener.onSetLongClick(false);
                    delete_set.clear();
                    notifyDataSetChanged();
                }
                else{
                    delete_view = true;
                    delete_set.add(sets.get(holder.getAdapterPosition()));
                    listener.onSetLongClick(true);
                    sets.get(holder.getAdapterPosition()).setSelected(true);
                    notifyDataSetChanged();
                }
                return true;
            }
        });

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SetStartActivity.class);
                intent.putExtra("mySet", sets.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }
}
