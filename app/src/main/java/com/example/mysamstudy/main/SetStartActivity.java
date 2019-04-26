package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SettingsManager;

public class SetStartActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TAG";

    public static boolean SHOW_ANSWERS, LOOP_SET;
    int current_card, ACTIVITY_NUM;
    CardFragment cardFragment;
    Set set;
    TextView set_title;
    Button next_btn;
    ImageView back_btn;
    Toolbar toolbar;

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
        setContentView(R.layout.activity_set_start);

        set_title = findViewById(R.id.set_title);
        next_btn = findViewById(R.id.next_btn);
        back_btn = findViewById(R.id.back_button);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);

        set = getIntent().getParcelableExtra("mySet");
        ACTIVITY_NUM = getIntent().getIntExtra("ACTIVITY_NUM", -1);

        if (set != null){
            current_card = 0;
            set_title.setText(set.getSetName());

            DatabaseManager dmb = new DatabaseManager(this);
            set.setCards(dmb.getCards(set.getSetId()));

            initUserPreferences();
            startSession();
        }
    }

    public void initUserPreferences(){
        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_loop);
        if (!SettingsManager.get_study_preferences(SettingsManager.study_preferences_loop))
            LOOP_SET = false;
        else
            LOOP_SET = true;

        SettingsManager.getSharedPreferences(this, SettingsManager.study_preferences_show_answer);
        if (!SettingsManager.get_study_preferences(SettingsManager.study_preferences_show_answer))
            SHOW_ANSWERS = false;
        else
            SHOW_ANSWERS = true;
    }

    public void startSession(){
        cardFragment = new CardFragment();
        Bundle args = new Bundle();
        args.putParcelable("card", set.getCards().get(current_card));
        cardFragment.setArguments(args);
        setFragment(cardFragment);
        current_card++;
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit(){
        switch(ACTIVITY_NUM){
            case(0):
                Intent main = new Intent(SetStartActivity.this, MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                finish();
                break;
            case(1):
                Intent favorites = new Intent(SetStartActivity.this, FavoritesActivity.class);
                favorites.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(favorites);
                finish();
                break;
            case(-1):
                Intent intent = new Intent(SetStartActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_set_menu, menu);
        if (LOOP_SET)
            menu.getItem(0).setChecked(true);
        if (SHOW_ANSWERS)
            menu.getItem(1).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case(R.id.loop_set):
                if (item.isChecked()){
                    LOOP_SET = false;
                    item.setChecked(false);
                }
                else{
                    LOOP_SET = true;
                    item.setChecked(true);
                }
                break;

            case(R.id.show_answer):
                if (item.isChecked()){
                    SHOW_ANSWERS = false;
                    item.setChecked(false);
                    current_card--;
                    startSession();
                }
                else {
                    SHOW_ANSWERS = true;
                    item.setChecked(true);
                    current_card--;
                    startSession();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.next_btn):
                if (current_card < set.getSetSize()){
                    startSession();
                }
                else if (current_card == set.getSetSize() && LOOP_SET){
                    current_card = 0;
                    startSession();
                }
                else{
                    Toast.makeText(this, "End of set", Toast.LENGTH_SHORT).show();
                }
                break;

            case(R.id.back_button):
                exit();
        }
    }
}
