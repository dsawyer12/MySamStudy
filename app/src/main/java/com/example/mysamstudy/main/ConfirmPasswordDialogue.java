package com.example.mysamstudy.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

public class ConfirmPasswordDialogue extends DialogFragment implements View.OnClickListener {

    Button verify_btn, cancel_btn;
    EditText password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialogue_confirm_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        password = view.findViewById(R.id.password_field);
        verify_btn = view.findViewById(R.id.verify_password_btn);
        cancel_btn = view.findViewById(R.id.cancel_verify_btn);
        verify_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
    }

    public void verifyPassword(){
        if (password.getText().toString().trim().isEmpty()){
            password.setError("Enter your password");
            password.requestFocus();
            return;
        }
        SettingsManager.getSharedPreferences(getActivity(), SettingsManager.user_session);
        Gson gson = new Gson();
        String jobj = SettingsManager.getUserSession(SettingsManager.user_session);
        User user = gson.fromJson(jobj, User.class);
        if (user != null){
            if (!password.getText().toString().trim().equals(user.getPassword())){
                password.setError("Invalid password");
                password.requestFocus();
            }
            else{
                DatabaseManager dbm = new DatabaseManager(getActivity());
                dbm.removeAccount(user);

                SettingsManager.getSharedPreferences(getActivity(), SettingsManager.user_session);
                SettingsManager.logOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(getActivity(), "Could not retrieve user information", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.verify_password_btn):
                verifyPassword();
                break;

            case(R.id.cancel_verify_btn):
                getDialog().dismiss();
                break;
        }
    }
}
