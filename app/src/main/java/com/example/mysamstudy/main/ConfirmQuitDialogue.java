package com.example.mysamstudy.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mysamstudy.R;

public class ConfirmQuitDialogue extends DialogFragment implements View.OnClickListener {

    Button confirm_quit, confirm_continue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogue_confirm_quit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirm_quit = view.findViewById(R.id.confirm_quit);
        confirm_continue = view.findViewById(R.id.confirm_continue);
        confirm_quit.setOnClickListener(this);
        confirm_continue.setOnClickListener(this);
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
            case(R.id.confirm_quit):
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case(R.id.confirm_continue):
                getDialog().dismiss();
                break;
        }
    }
}
