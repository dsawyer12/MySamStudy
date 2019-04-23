package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SettingsManager;

import java.text.SimpleDateFormat;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TAG";

    EditText fName, lName, username, email, password;
    Button login, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences)){
            setTheme(R.style.AppThemeLight);
        }
        else{
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        fName = findViewById(R.id.register_fname);
        lName = findViewById(R.id.register_lname);
        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        login= findViewById(R.id.register_login);
        submit = findViewById(R.id.register_submit);

        login.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    public void verifyCredentials(){
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM:dd:yyyy");
        String dateString = sdf.format(date);
        String firstName, lastName, mUsername, mEmail, mPassword;
        firstName = fName.getText().toString().trim();
        lastName = lName.getText().toString().trim();
        mUsername = username.getText().toString().trim();
        mEmail = email.getText().toString().trim();
        mPassword = password.getText().toString().trim();

        if (firstName.isEmpty()){
            fName.setError("Field is empty");
            fName.requestFocus();
            return;
        }
        if (lastName.isEmpty()){
            lName.setError("Field is empty");
            lName.requestFocus();
            return;
        }
        if (mUsername.isEmpty()){
            username.setError("Field is empty");
            username.requestFocus();
            return;
        }
        if (mEmail.isEmpty()){
            email.setError("Field is empty");
            email.requestFocus();
            return;
        }
        if (mPassword.isEmpty()){
            password.setError("Field is empty");
            password.requestFocus();
            return;
        }
        if (mPassword.length() < 6){
            password.setError("6 characters minimum");
            password.requestFocus();
            return;
        }

        DatabaseManager dbm = new DatabaseManager(this);
        boolean taken = dbm.checkUsername(mUsername);
        if (taken){
            username.setError("This username is already taken");
            username.requestFocus();
            return;
        }

        User user = new User(firstName, lastName, mUsername, mEmail, mPassword, dateString);
        long id = dbm.addUser(user);
        if (id <= 0)
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
//            setDefaultSettings();
            login();
        }
    }

//    public void setDefaultSettings(){
//        SettingsManager.getSharedPreferences(this, SettingsManager.share_selection_preferences);
//        SettingsManager.write(SettingsManager.share_selection_preferences, 0);
//    }

    public void login(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.register_login):
                login();
                break;

            case(R.id.register_submit):
                verifyCredentials();
                break;
        }
    }
}
