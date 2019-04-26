package com.example.mysamstudy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;

import java.util.ArrayList;

public class CardListAdapter extends BaseAdapter {

    private ArrayList<Card> cards;
    private Context context;
    private LayoutInflater layoutInflater;

    public CardListAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cards.size();
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
            convertView = layoutInflater.inflate(R.layout.snippet_card_item, parent, false);
            holder.card_question = convertView.findViewById(R.id.card_question);
            holder.card_answer = convertView.findViewById(R.id.card_answer);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.card_question.setText(cards.get(position).getCardQuestion());
        holder.card_answer.setText(cards.get(position).getCardAnswer());
        return convertView;
    }

    public class ViewHolder{
        TextView card_question, card_answer;
    }
}
