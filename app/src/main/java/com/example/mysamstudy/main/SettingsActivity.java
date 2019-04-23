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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SetSelectShareAdapter;
import com.example.mysamstudy.utils.SettingsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "TAG";

    Switch dark_theme_switch;
    Button save_changes;
    CheckBox loop_set, show_answer;
    ImageView back_btn, share_list;
    RadioGroup share_radio_group;
    RadioButton share_all, select_share, share_none;
    ListView list;
    SetSelectShareAdapter.OnItemCheckedListener listener;
    SetSelectShareAdapter adapter;

    ArrayList<Set> sets;
    Map<Integer, Boolean> updatedSets;

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
        share_radio_group = findViewById(R.id.share_radio_group);
        share_all = findViewById(R.id.share_all);
        select_share = findViewById(R.id.select_share);
        share_none = findViewById(R.id.share_none);
        list = findViewById(R.id.listview);
        share_list = findViewById(R.id.share_list);

        back_btn.setOnClickListener(this);
        save_changes.setOnClickListener(this);
        share_list.setOnClickListener(this);
        share_radio_group.setOnCheckedChangeListener(this);

        sets = new ArrayList<>();
        updatedSets = new HashMap<>();

        initUserPreferences();
        initSettings();
    }

    public void initUserPreferences(){
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences))
            dark_theme_switch.setChecked(false);
        else
            dark_theme_switch.setChecked(true);

        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_loop);
        loop_set.setChecked(SettingsManager.get_study_preferences(SettingsManager.study_preferences_loop));

        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_show_answer);
        show_answer.setChecked(SettingsManager.get_study_preferences(SettingsManager.study_preferences_show_answer));

        dark_theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDarkTheme(isChecked);
            }
        });
    }

    public void initSettings(){
        SettingsManager.getSharedPreferences(this, SettingsManager.share_selection_preferences);
        int shareSelectedOption = SettingsManager.getShareSelectionPreferences(SettingsManager.share_selection_preferences);
        share_radio_group.indexOfChild(findViewById(share_radio_group.getCheckedRadioButtonId()));
        share_radio_group.check(shareSelectedOption);
        if (shareSelectedOption == select_share.getId()){
            share_list.setVisibility(View.VISIBLE);
            share_list.setImageResource(R.drawable.ic_collapse);
            list.setVisibility(View.VISIBLE);
        }
        else{
            share_list.setVisibility(View.GONE);
            share_list.setImageResource(R.drawable.ic_expand);
            list.setVisibility(View.GONE);
        }

        sets = getIntent().getParcelableArrayListExtra("sets");
        if (sets != null){

            listener = new SetSelectShareAdapter.OnItemCheckedListener() {
                @Override
                public void onChecked(int position) {
                    Log.d(TAG, Boolean.toString(sets.get(position).isShare()));
                    sets.get(position).setShare(true);
                    updatedSets.put(sets.get(position).getSetId(), true);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onUnchecked(int position) {
                    Log.d(TAG, Boolean.toString(sets.get(position).isShare()));
                    sets.get(position).setShare(false);
                    updatedSets.put(sets.get(position).getSetId(), false);
                    adapter.notifyDataSetChanged();
                }
            };

            adapter = new SetSelectShareAdapter(this, sets, listener);
            list.setAdapter(adapter);
        }
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
        DatabaseManager dbm = new DatabaseManager(this);
        for (Map.Entry<Integer, Boolean> entry : updatedSets.entrySet())
            dbm.updateSharedSetList(entry.getKey(), entry.getValue());

        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_loop);
        SettingsManager.write(SettingsManager.study_preferences_loop, loop_set.isChecked());

        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_show_answer);
        SettingsManager.write(SettingsManager.study_preferences_show_answer, show_answer.isChecked());
        
        exit();
    }

    public void refresh(){
        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
        intent.putParcelableArrayListExtra("sets", sets);
        startActivity(intent);
        finish();
    }

    public void exit(){
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

            case(R.id.share_list):
                if (list.isShown()){
                    share_list.setImageResource(R.drawable.ic_expand);
                    list.setVisibility(View.GONE);
                }
                else{
                    share_list.setImageResource(R.drawable.ic_collapse);
                    list.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        SettingsManager.getSharedPreferences(this, SettingsManager.share_selection_preferences);
        SettingsManager.write(SettingsManager.share_selection_preferences, checkedId);
        if (SettingsManager.getShareSelectionPreferences(SettingsManager.share_selection_preferences) == select_share.getId()){
            share_list.setVisibility(View.VISIBLE);
            share_list.setImageResource(R.drawable.ic_collapse);
            list.setVisibility(View.VISIBLE);
        }
        else{
            share_list.setVisibility(View.GONE);
            share_list.setImageResource(R.drawable.ic_expand);
            list.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}

