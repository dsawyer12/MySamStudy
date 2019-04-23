package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.mysamstudy.R;
import com.example.mysamstudy.utils.SettingsManager;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Switch dark_theme_switch;
    Button save_changes;
    ImageView back_btn;
    CheckBox loop_set, show_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences)){
            setTheme(R.style.AppThemeLight);
        }
        else {
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dark_theme_switch = findViewById(R.id.dark_theme_switch);
        save_changes = findViewById(R.id.save_changes);
        back_btn = findViewById(R.id.back_btn);
        loop_set = findViewById(R.id.loop_set_checkbox);
        show_answer = findViewById(R.id.show_answer_checkox);
        back_btn.setOnClickListener(this);
        save_changes.setOnClickListener(this);

        initUserPreferences();
    }

    public void initUserPreferences(){
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences))
            dark_theme_switch.setChecked(false);
        else
            dark_theme_switch.setChecked(true);

        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_loop);
        if (!SettingsManager.get_study_preferences(SettingsManager.study_preferences_loop))
            loop_set.setChecked(false);
        else
            loop_set.setChecked(true);

        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_show_answer);
        if (!SettingsManager.get_study_preferences(SettingsManager.study_preferences_show_answer))
            show_answer.setChecked(false);
        else
            show_answer.setChecked(true);

        dark_theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDarkTheme(isChecked);
            }
        });
    }

    public void setDarkTheme(boolean isChecked){
        if (isChecked){
            SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
            SettingsManager.write(SettingsManager.dark_theme_preferences,true);
            refresh();
        }
        else{
            SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
            SettingsManager.write(SettingsManager.dark_theme_preferences,false);
            refresh();
        }
    }

    public void saveChanges(){
        if (loop_set.isChecked()) {
            SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_loop);
            SettingsManager.write(SettingsManager.study_preferences_loop, true);
        }
        else {
            SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_loop);
            SettingsManager.write(SettingsManager.study_preferences_loop, false);
        }

        if (show_answer.isChecked()){
            SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_show_answer);
            SettingsManager.write(SettingsManager.study_preferences_show_answer, true);
        }
        else{
            SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_show_answer);
            SettingsManager.write(SettingsManager.study_preferences_show_answer, false);
        }

        exit();
    }

    public void refresh(){
        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void exit(){
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.save_changes):
                saveChanges();
                break;

            case(R.id.back_btn):
                exit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

