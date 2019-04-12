package com.example.mysamstudy.main;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;

public class UpdatePasswordDialogue extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "TAG";

    EditText current_p, new_p, confirm_p;
    Button save_changes, cancel_change;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialogue_update_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SettingsManager.getSharedPreferences(getActivity(), SettingsManager.user_session);
        Gson gson = new Gson();
        String jobj = SettingsManager.getUserSession(SettingsManager.user_session);
        user = gson.fromJson(jobj, User.class);

        current_p = view.findViewById(R.id.current_password);
        new_p = view.findViewById(R.id.new_password);
        confirm_p = view.findViewById(R.id.new_password_confirm);

        save_changes = view.findViewById(R.id.save_password_btn);
        cancel_change = view.findViewById(R.id.cancel_pasw_change_btn);
        save_changes.setOnClickListener(this);
        cancel_change.setOnClickListener(this);
    }

    public void verifyCredentials(){

        if (new_p.getText().toString().trim().length() < 6){
            new_p.setError("minimum of 6 characters");
            new_p.requestFocus();
            return;
        }

        if (!confirm_p.getText().toString().trim().equals(new_p.getText().toString().trim())){
            confirm_p.setError("Password's do not match");
            confirm_p.requestFocus();
            return;
        }

        if (!current_p.getText().toString().trim().equals(user.getPassword())){
            current_p.setError("Invalid");
            current_p.requestFocus();
            return;
        }

        if (new_p.getText().toString().trim().equals(user.getPassword())){
            new_p.setError("New Password can't be your current one");
            new_p.requestFocus();
            return;
        }

        DatabaseManager dbm = new DatabaseManager(getActivity());
        dbm.updateUserPassword(user, new_p.getText().toString().trim());
        user.setPassword(new_p.getText().toString().trim());
        Gson gson = new Gson();
        String jobj = gson.toJson(user);
        SettingsManager.loadUserSession(SettingsManager.user_session, jobj);
        Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
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
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.save_password_btn):
                verifyCredentials();
                break;

            case(R.id.cancel_pasw_change_btn):
                getDialog().dismiss();
                break;
        }
    }
}


