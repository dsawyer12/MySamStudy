package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TAG";

    EditText username, password;
    Button login, register;
    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Login");
        SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences)){
            setTheme(R.style.AppThemeLight);
        }
        else{
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
        if (SettingsManager.getUserSession(SettingsManager.user_session) != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        dbm = new DatabaseManager(this);

        username = findViewById(R.id.login_username_field);
        password = findViewById(R.id.login_password_field);
        login = findViewById(R.id.login_button);
        register = findViewById(R.id.login_register_button);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void verifyCredentials() {
        String mUsername = username.getText().toString().trim();
        String mPassword = password.getText().toString().trim();

        if (mUsername.isEmpty()){
            username.setError("Enter user email");
            username.requestFocus();
            return;
        }
        if (mPassword.isEmpty()){
            password.setError("Enter user password");
            password.requestFocus();
            return;
        }

        dbm = new DatabaseManager(this);
        User user = dbm.getUser(mUsername, mPassword);
        if (user != null){
            Gson gson = new Gson();
            String json = gson.toJson(user);
            SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
            SettingsManager.loadUserSession(SettingsManager.user_session, json);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Could not retrieve user. Check your username and password and try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.login_button):
                verifyCredentials();
                break;

            case(R.id.login_register_button):
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
