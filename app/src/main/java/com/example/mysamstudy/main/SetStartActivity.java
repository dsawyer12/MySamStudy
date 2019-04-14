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

    int current_card;
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

        if (set != null){
            current_card = 0;
            set_title.setText(set.getSetName());

            DatabaseManager dmb = new DatabaseManager(this);
            set.setCards(dmb.getCards(set.getSetId()));

            startSession();
        }
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
//        ConfirmQuitdialog dialog = new ConfirmQuitdialog();
//        dialog.show(getSupportFragmentManager(), "quitDialog");
    }

    public void exit(){
        Intent intent = new Intent(SetStartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_set_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case(R.id.loop_set):
                if (item.isChecked()){
                    item.setChecked(false);
                    set.setLoop_set(false);
                }
                else{
                    item.setChecked(true);
                    set.setLoop_set(true);
                }
                break;

            case(R.id.show_answer):
                if (item.isChecked()){
                    item.setChecked(false);
                    set.setAnswerAlwaysOn(false);
                    current_card--;
                    startSession();
                }
                else{
                    item.setChecked(true);
                    set.setAnswerAlwaysOn(true);
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
                else if (current_card == set.getSetSize() && set.isLoop_set()){
                    current_card = 0;
                    startSession();
                }
                else{
                    Toast.makeText(this, "End of set", Toast.LENGTH_SHORT).show();
                }
                break;

            case(R.id.back_button):
                exit();
//                ConfirmQuitdialog dialog = new ConfirmQuitdialog();
//                dialog.show(getSupportFragmentManager(), "quitDialog");
//                break;
        }
    }
}
