package com.example.mysamstudy.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;

public class CardFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TAG";

    Card card;
    TextView card_question, card_answer;
    Button show_answer;
    boolean shown = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        card_question = view.findViewById(R.id.card_question);
        card_answer = view.findViewById(R.id.card_answer);
        show_answer = view.findViewById(R.id.show_answer);
        show_answer.setOnClickListener(this);

        card = getCardFromBundle();
        if (card != null){
            card_question.setText(card.getCardQuestion());
            card_answer.setText(card.getCardAnswer());
        }

        initPreferences();
    }

    public void initPreferences(){
        if (SetStartActivity.SHOW_ANSWERS){
            card_answer.setVisibility(View.VISIBLE);
            show_answer.setVisibility(View.GONE);
        }
        else{
            card_answer.setVisibility(View.GONE);
            show_answer.setVisibility(View.VISIBLE);
        }
    }

    public Card getCardFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelable("card");
        }
        else{
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.show_answer):
                if (shown){
                    shown = false;
                    card_answer.setVisibility(View.GONE);
                    show_answer.setText("Show Answer");
                }
                else{
                    shown = true;
                    card_answer.setVisibility(View.VISIBLE);
                    show_answer.setText("Hide Answer");
                }
                break;
        }
    }
}
