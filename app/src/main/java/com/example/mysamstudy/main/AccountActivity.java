package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SetSelectShareAdapter;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener,
        SetSelectShareAdapter.OnItemCheckedListener{
    private static final String TAG = "TAG";

    ImageView back_btn;
    TextView account_created;
    EditText first_name, last_name, username, email;
    Button password, save_changes, delete_account;

    private User user;

    @Override
    public void onChecked(int position) { }

    @Override
    public void onUnchecked(int position) { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences))
            setTheme(R.style.AppThemeLight);
        else
            setTheme(R.style.AppThemeDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        account_created = findViewById(R.id.account_created);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        back_btn = findViewById(R.id.back_btn);
        delete_account = findViewById(R.id.delete_account_btn);
        password = findViewById(R.id.password);
        save_changes = findViewById(R.id.save_changes);

        back_btn.setOnClickListener(this);
        password.setOnClickListener(this);
        delete_account.setOnClickListener(this);
        save_changes.setOnClickListener(this);

        SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
        Gson gson = new Gson();
        String jobj = SettingsManager.getUserSession(SettingsManager.user_session);
        user = gson.fromJson(jobj, User.class);

        first_name.setText(user.getFirst_name());
        last_name.setText(user.getLast_name());
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        account_created.setText("Account Created - " + user.getRegister_date());
    }

    public void verifySettings(){
        DatabaseManager dbm = new DatabaseManager(this);
        SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
        Gson gson = new Gson();
        if (!first_name.getText().toString().trim().equals(user.getFirst_name())){
            dbm.updateUserFirstName(user, first_name.getText().toString().trim());
            user.setFirst_name(first_name.getText().toString().trim());
            String jobj = gson.toJson(user);
            SettingsManager.loadUserSession(SettingsManager.user_session, jobj);
        }
        if (!last_name.getText().toString().trim().equals(user.getLast_name())){
            dbm.updateUserLastName(user, last_name.getText().toString().trim());
            user.setLast_name(last_name.getText().toString().trim());
            String jobj = gson.toJson(user);
            SettingsManager.loadUserSession(SettingsManager.user_session, jobj);
        }
        if (!username.getText().toString().trim().equals(user.getUsername())){
            dbm.updateUserUsername(user, username.getText().toString().trim());
            user.setUsername(username.getText().toString().trim());
            String jobj = gson.toJson(user);
            SettingsManager.loadUserSession(SettingsManager.user_session, jobj);
        }
        if(!email.getText().toString().trim().equals(user.getEmail())){
            dbm.updateUserEmail(user, email.getText().toString().trim());
            user.setEmail(email.getText().toString().trim());
            String jobj = gson.toJson(user);
            SettingsManager.loadUserSession(SettingsManager.user_session, jobj);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.back_btn):
               exit();
                break;

            case(R.id.password):
                UpdatePasswordDialogue dialog = new UpdatePasswordDialogue();
                dialog.show(getSupportFragmentManager(), "updatePassword");
                break;

            case(R.id.save_changes):
                verifySettings();
                exit();
                break;

            case(R.id.delete_account_btn):
                ConfirmDeleteAccountDialogue deleteAccountDialogue = new ConfirmDeleteAccountDialogue();
                deleteAccountDialogue.show(getSupportFragmentManager(), "delete_account_dialog");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit(){
        Intent intent1 = new Intent(AccountActivity.this, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
        finish();
    }
}
