package com.example.mysamstudy.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.utils.DatabaseManager;

public class EditCardFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "TAG";

    TextView toolbar_header;
    EditText card_question, card_answer;
    ImageView edit_card_finish, edit_card_back, edit_card_delete;
    Card card;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar_header = view.findViewById(R.id.edit_card_toolbar_title);
        card_question = view.findViewById(R.id.card_question);
        card_answer=  view.findViewById(R.id.card_answer);
        edit_card_finish = view.findViewById(R.id.edit_card_finish);
        edit_card_back = view.findViewById(R.id.edit_card_back);
        edit_card_delete = view.findViewById(R.id.edit_card_delete);
        edit_card_delete.setVisibility(View.VISIBLE);

        edit_card_finish.setOnClickListener(this);
        edit_card_back.setOnClickListener(this);
        edit_card_delete.setOnClickListener(this);

        card = getCardFromBundle();
        if (card != null)
            initLayout();
    }

    public void initLayout(){
        card_question.setText(card.getCardQuestion());
        card_answer.setText(card.getCardAnswer());
        card_question.requestFocus();
    }

    public void updateCard(){
        if (card_question.getText().toString().trim().isEmpty()){
            card_question.setError("Field is empty");
            card_question.requestFocus();
            return;
        }
        if (card_answer.getText().toString().trim().isEmpty()){
            card_answer.setError("Field is empty");
            card_answer.requestFocus();
        }

        else{
            card.setCardQuestion(card_question.getText().toString().trim());
            card.setCardAnswer(card_answer.getText().toString().trim());

            DatabaseManager dbm = new DatabaseManager(getActivity());
            dbm.updateCard(card);
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.edit_card_finish):
                updateCard();
                break;

            case(R.id.edit_card_back):
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;

            case(R.id.edit_card_delete):
                DeleteCardDialogue dialogue = new DeleteCardDialogue();
                Bundle args = new Bundle();
                args.putParcelable("deleteCard", card);
                dialogue.setArguments(args);
                dialogue.show(getActivity().getSupportFragmentManager(), "dialogue");
                break;
        }
    }

    public Card getCardFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelable("editCard");
        }
        else{
            return null;
        }
    }
}
