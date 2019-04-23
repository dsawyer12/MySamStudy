package com.example.mysamstudy.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;

public class DeleteCardDialogue extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "TAG";

    TextView cancel, confirm;
    Card card;
    OnDeleteCard listener;

    public interface OnDeleteCard{
        void onDelete(int cardId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogue_delete_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        card = getCardFromBundle();

        cancel = view.findViewById(R.id.cancel);
        confirm = view.findViewById(R.id.confirm);

        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    public Card getCardFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null)
            return bundle.getParcelable("deleteCard");
        else
            return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnDeleteCard) getActivity();
        }
        catch(Exception e){
            Log.d(TAG, "onAttach: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case(R.id.cancel):
                getDialog().dismiss();
                break;

            case(R.id.confirm):
                if (card != null){
                    listener.onDelete(card.getCardID());
                    getDialog().dismiss();
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
                else
                    getDialog().dismiss();
                break;
        }
    }
}
















