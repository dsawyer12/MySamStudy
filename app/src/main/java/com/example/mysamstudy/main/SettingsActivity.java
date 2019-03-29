package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.mysamstudy.R;
import com.example.mysamstudy.utils.SettingsManager;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Switch dark_theme_switch;
    Button save_changes;
    ImageView back_btn;

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
        back_btn.setOnClickListener(this);

        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences)){
            dark_theme_switch.setChecked(false);
        }
        else {
            dark_theme_switch.setChecked(true);
        }

        save_changes.setOnClickListener(this);

        dark_theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SettingsManager.write(SettingsManager.dark_theme_preferences,true);
                    refresh();
                }
                else{
                    SettingsManager.write(SettingsManager.dark_theme_preferences,false);
                    refresh();
                }
            }
        });
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
                // save to database
                exit();
                break;

            case(R.id.back_btn):
                exit();
                break;
        }
    }
}
