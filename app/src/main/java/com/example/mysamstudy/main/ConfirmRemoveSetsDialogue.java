package com.example.mysamstudy.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SetListRecyclerView;

import java.util.ArrayList;

public class ConfirmRemoveSetsDialogue extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "TAG";

    ArrayList<Set> delete_set;

    RecyclerView recyclerView;
    SetListRecyclerView adapter;
    SetListRecyclerView.OnItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialogue_confirm_remove_sets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        Button confirm_btn = view.findViewById(R.id.confirm_btn),
                cancel_btn = view.findViewById(R.id.cancel_btn);

        confirm_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

        delete_set = getSetFromBundle();

        adapter = new SetListRecyclerView(getActivity(), -1, delete_set, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                DatabaseManager dbm = new DatabaseManager(getActivity());
                for (int i = 0; i < delete_set.size(); i++){
                    dbm.deleteSet(delete_set.get(i).getSetId());
                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getDialog().dismiss();
                break;

            case(R.id.cancel_btn):
                getDialog().dismiss();
                break;
        }
    }
}
