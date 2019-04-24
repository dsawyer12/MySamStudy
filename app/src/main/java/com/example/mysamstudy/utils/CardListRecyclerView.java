package com.example.mysamstudy.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.objects.Set;

import java.util.ArrayList;

public class CardListRecyclerView extends RecyclerView.Adapter<CardListRecyclerView.ViewHolder> {
    private static final String TAG = "TAG";

    private Context context;
    private Set set;
    private boolean delete_view, clickable;
    private ArrayList<Card> delete_set;
    private OnCardClickListener listener;

    public interface OnCardClickListener{
        void onLongCLick(boolean is_delete_view);
        void onClick(Card card);
    }

    public CardListRecyclerView(Context context, Set set, OnCardClickListener listener) {
        this.context = context;
        this.set = set;
        this.listener = listener;
        delete_set = new ArrayList<>();
        clickable = true;
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

    public void clearDeleteSet(){
        delete_set.clear();
        for (int i = 0; i < set.getCards().size(); i++){
            set.getCards().get(i).setSelected(false);
        }
    }

    public void setClickable(boolean clickable){
        this.clickable = clickable;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView card_question, card_answer, card_number;
        LinearLayout rootView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_question = itemView.findViewById(R.id.card_question);
            card_answer = itemView.findViewById(R.id.card_answer);
            card_number = itemView.findViewById(R.id.card_number);
            rootView = itemView.findViewById(R.id.rootView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (set.getSetSize() != 0){
            holder.card_question.setText(set.getCards().get(position).getCardQuestion());
            holder.card_answer.setText(set.getCards().get(position).getCardAnswer());
            holder.card_number.setText(String.valueOf(position + 1));
        }

        if (delete_view){
            holder.checkBox.setVisibility(View.VISIBLE);
            if (set.getCards().get(holder.getAdapterPosition()).isSelected())
                holder.checkBox.setChecked(true);
            else
                holder.checkBox.setChecked(false);
        }
        else{
            delete_set.clear();
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }

        if (clickable){
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (delete_view){
                        if (set.getCards().get(holder.getAdapterPosition()).isSelected()){
                            set.getCards().get(holder.getAdapterPosition()).setSelected(false);
                            holder.checkBox.setChecked(false);
                            delete_set.remove(set.getCards().get(holder.getAdapterPosition()));
                        }
                        else{
                            set.getCards().get(holder.getAdapterPosition()).setSelected(true);
                            holder.checkBox.setChecked(true);
                            delete_set.add(set.getCards().get(holder.getAdapterPosition()));
                        }
                    }
                    else{
                        listener.onClick(set.getCards().get(holder.getAdapterPosition()));
                    }
                }
            });

            holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (delete_view){
                        delete_view = false;
                        clearDeleteSet();
                        listener.onLongCLick(delete_view);
                    }
                    else{
                        delete_view = true;
                        set.getCards().get(holder.getAdapterPosition()).setSelected(true);
                        delete_set.add(set.getCards().get(holder.getAdapterPosition()));
                        listener.onLongCLick(delete_view);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return set.getSetSize();
    }
}
