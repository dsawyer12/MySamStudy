package com.example.mysamstudy.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.objects.Set;

import java.util.ArrayList;

public class SetListAdapter extends BaseAdapter {
    private static final String TAG = "TAG";
    private Set set;
    private LayoutInflater inflater;
    private OnCardClickListener listener;
    private boolean delete_view;
    private ArrayList<Card> delete_set;

    public interface OnCardClickListener{
        void onLongCLick(boolean is_delete_view);
        void onClick(Card card);
    }

    public SetListAdapter(Context context, Set set, OnCardClickListener listener) {
        Log.d(TAG, "SetListAdapter: created");
        this.inflater = LayoutInflater.from(context);
        Context context1 = context;
        this.set = set;
        this.listener = listener;
        this.delete_set = new ArrayList<>();
        this.delete_view = false;
    }

    public void updateSet(){
        set.getCards().removeAll(delete_set);
        delete_set.clear();
        setIs_deleteView(false);
        Log.d(TAG, "set size now : " + String.valueOf(set.getSetSize()));
    }

    public void setIs_deleteView(boolean delete_view){
        this.delete_view = delete_view;
    }

    public ArrayList<Card> getDelete_set(){
        return delete_set;
    }

    @Override
    public int getCount() {
        return set.getSetSize();
    }

    @Override
    public Object getItem(int position) {
        return set.getCards().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.snippet_card_item, parent, false);
            holder.set_question = convertView.findViewById(R.id.card_question);
            holder.set_answer = convertView.findViewById(R.id.card_answer);
            holder.card_number = convertView.findViewById(R.id.card_number);
            holder.rootView = convertView.findViewById(R.id.rootView);
            holder.checkBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (set.getSetSize() != 0){
            holder.set_question.setText(set.getCards().get(position).getCardQuestion());
            holder.set_answer.setText(set.getCards().get(position).getCardAnswer());
            holder.card_number.setText(String.valueOf(position + 1));
        }

        if (delete_view){
            Log.d(TAG, "delete view : true");
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else{
            Log.d(TAG, "delete view : false");
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delete_view){
                    if (holder.checkBox.isChecked()){
                        holder.checkBox.setChecked(false);
                        delete_set.remove(set.getCards().get(position));
                    }
                    else{
                        holder.checkBox.setChecked(true);
                        delete_set.add(set.getCards().get(position));
                    }
                }
                else{
                    listener.onClick(set.getCards().get(position));
                }
            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (delete_view){
                    delete_view = false;
                    listener.onLongCLick(delete_view);
                }
                else{
                    delete_view = true;
                    holder.checkBox.setChecked(true);
                    delete_set.add(set.getCards().get(position));
                    listener.onLongCLick(delete_view);
                }
                return true;
            }
        });

        return convertView;
    }

    public class ViewHolder{
        TextView set_question, set_answer, card_number;
        LinearLayout rootView;
        CheckBox checkBox;
    }
}
