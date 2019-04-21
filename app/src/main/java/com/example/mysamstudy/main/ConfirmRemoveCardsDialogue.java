package com.example.mysamstudy.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.utils.BaseCardListAdapter;
import com.example.mysamstudy.utils.DatabaseManager;

import java.util.ArrayList;

public class ConfirmRemoveCardsDialogue extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "TAG";
    ArrayList<Card> delete_set;
    OnCardsReomved listener;

    public interface OnCardsReomved{
        void onRemove(boolean remove);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialogue_confirm_remove_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button confirm_btn = view.findViewById(R.id.confirm_btn),
                cancel_btn = view.findViewById(R.id.cancel_btn);
        ListView listview = view.findViewById(R.id.remove_list);

        confirm_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

        delete_set = getSetFromBundle();

        if(delete_set != null){
            BaseCardListAdapter adapter = new BaseCardListAdapter(getActivity(), delete_set);
            listview.setAdapter(adapter);
        }
    }

    public ArrayList<Card> getSetFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelableArrayList("delete_set");
        }
        else
            return null;
    }

    @Override
    public void onAttach(Context context) {
        try {
            listener = (OnCardsReomved) getActivity();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.confirm_btn):
                listener.onRemove(true);
                getDialog().dismiss();
                break;

            case(R.id.cancel_btn):
                getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
