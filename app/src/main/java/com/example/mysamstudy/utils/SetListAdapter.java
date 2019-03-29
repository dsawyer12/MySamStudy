package com.example.mysamstudy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;

public class SetListAdapter extends BaseAdapter {
    private static final String TAG = "TAG";
    private Context context;
    private Set set;
    private LayoutInflater inflater;

    public SetListAdapter(Context context, Set set) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.set = set;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.snippet_card_item, parent, false);
            holder.set_question = convertView.findViewById(R.id.card_question);
            holder.set_answer = convertView.findViewById(R.id.card_answer);
            holder.card_number = convertView.findViewById(R.id.card_number);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.set_question.setText(set.getCards().get(position).getCardQuestion());
        holder.set_answer.setText(set.getCards().get(position).getCardAnswer());
        holder.card_number.setText(String.valueOf(position + 1));
        return convertView;
    }

    public class ViewHolder{
        TextView set_question, set_answer, card_number;
    }
}
