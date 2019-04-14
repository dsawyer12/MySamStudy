package com.example.mysamstudy.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
        RadioGroup.OnCheckedChangeListener,
        SetSelectShareAdapter.OnItemCheckedListener{
    private static final String TAG = "TAG";

    ImageView back_btn, share_list;
    TextView account_created;
    EditText first_name, last_name, username, email;
    RadioGroup share_radio_group;
    RadioButton share_all, select_share, share_none;
    ListView list;
    Button password, save_changes, delete_account;
    SetSelectShareAdapter.OnItemCheckedListener listener;
    SetSelectShareAdapter adapter;

    private User user;
    ArrayList<Set> sets = new ArrayList<>();
    java.util.Set<Integer> selectedSet = new HashSet<>();

    @Override
    public void onChecked(int position) { }

    @Override
    public void onUnchecked(int position) { }

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
        setContentView(R.layout.activity_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        account_created = findViewById(R.id.account_created);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        share_radio_group = findViewById(R.id.share_radio_group);
        share_all = findViewById(R.id.share_all);
        select_share = findViewById(R.id.select_share);
        share_none = findViewById(R.id.share_none);
        list = findViewById(R.id.listview);
        back_btn = findViewById(R.id.back_btn);
        delete_account = findViewById(R.id.delete_account_btn);
        share_list = findViewById(R.id.share_list);
        password = findViewById(R.id.password);
        save_changes = findViewById(R.id.save_changes);

        back_btn.setOnClickListener(this);
        share_list.setOnClickListener(this);
        password.setOnClickListener(this);
        delete_account.setOnClickListener(this);
        save_changes.setOnClickListener(this);
        share_radio_group.setOnCheckedChangeListener(this);

        SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
        Gson gson = new Gson();
        String jobj = SettingsManager.getUserSession(SettingsManager.user_session);
        user = gson.fromJson(jobj, User.class);

        first_name.setText(user.getFirst_name());
        last_name.setText(user.getLast_name());
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        account_created.setText("Account Created - " + user.getRegister_date());

        initializeSettings();
    }

    public void initializeSettings(){
        SettingsManager.getSharedPreferences(this, SettingsManager.share_selection_preferences);
        int shareSelectedOption = SettingsManager.getShareSelectionPreferences(SettingsManager.share_selection_preferences);
        share_radio_group.indexOfChild(findViewById(share_radio_group.getCheckedRadioButtonId()));
        share_radio_group.check(shareSelectedOption);
        if (shareSelectedOption == select_share.getId()){
            share_list.setVisibility(View.VISIBLE);
        }
        else{
            share_list.setVisibility(View.GONE);
        }

        SettingsManager.getSharedPreferences(this, SettingsManager.user_session);

        sets = getIntent().getParcelableArrayListExtra("sets");
        if (sets != null){
            listener = new SetSelectShareAdapter.OnItemCheckedListener() {
                @Override
                public void onChecked(int position) {
                    selectedSet.add(sets.get(position).getSetId());
//                    SettingsManager.write(SettingsManager.share_selected_items, selectedSet);

                    // set the 'share' variable of this set appropriately in the database

                    sets.get(position).setShare(true);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onUnchecked(int position) {
                    selectedSet.remove(sets.get(position).getSetId());
//                    SettingsManager.write(SettingsManager.share_selected_items, selectedSet);

                    // set the 'share' variable of this set appropriately in the database

                    sets.get(position).setShare(false);
                    adapter.notifyDataSetChanged();

                }
            };
        }

        SettingsManager.getSharedPreferences(this, SettingsManager.share_selected_items);
        if (SettingsManager.getSetList(SettingsManager.share_selected_items) != null){
            java.util.Set<Integer> hashSet = SettingsManager.getSetList(SettingsManager.share_selected_items);
            for (int st : hashSet){
                for (int i = 0; i < sets.size(); i++){
                    if (sets.get(i).getSetId() == st){
                        sets.get(i).setShare(true);
                    }
                }
            }
        }

        if (sets != null){
            adapter = new SetSelectShareAdapter(this, sets, listener);
            list.setAdapter(adapter);
        }
    }

    public void verifySettings(){
        SettingsManager.getSharedPreferences(this, SettingsManager.share_selected_items);
        SettingsManager.write(SettingsManager.share_selected_items, selectedSet);
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

            case(R.id.delete_account_btn):
                ConfirmDeleteAccountDialogue deleteAccountDialogue = new ConfirmDeleteAccountDialogue();
                deleteAccountDialogue.show(getSupportFragmentManager(), "delete_account_dialog");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        SettingsManager.getSharedPreferences(this, SettingsManager.share_selection_preferences);
        SettingsManager.write(SettingsManager.share_selection_preferences, checkedId);
        if (SettingsManager.getShareSelectionPreferences(SettingsManager.share_selection_preferences) == select_share.getId()){
            share_list.setVisibility(View.VISIBLE);
        }
        else{
            share_list.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            share_list.setImageResource(R.drawable.ic_expand);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit(){
        Intent intent1 = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }
}
