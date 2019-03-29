package com.example.mysamstudy.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.utils.BaseSetListAdapter;

import java.util.ArrayList;

public class ConfirmRemoveSetsDialogue extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogue_confirm_remove_sets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Set> delete_set = getSetFromBundle();
        Log.d(TAG, delete_set.get(0).getSetName());
        Button confirm_btn = view.findViewById(R.id.confirm_btn),
                cancel_btn = view.findViewById(R.id.cancel_btn);
        ListView listview = view.findViewById(R.id.remove_list);

        confirm_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

        BaseSetListAdapter adapter = new BaseSetListAdapter(getActivity(), delete_set);
        listview.setAdapter(adapter);
    }

    public ArrayList<Set> getSetFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelableArrayList("delete_set");
        }
        else{
            return null;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.confirm_btn):

                // THIS IS WHERE YOU WILL DELETE THE SETS FROM THE DATABASE.

                getDialog().dismiss();
                break;

            case(R.id.cancel_btn):
                getDialog().dismiss();
                break;
        }
    }
}
