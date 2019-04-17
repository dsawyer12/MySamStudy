package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SearchListAdapter;
import com.example.mysamstudy.utils.SettingsManager;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG";
    private static final int SEARCH_SETS = 0;
    private static final int SEARCH_CARDS = 1;

    EditText search_box;
    TextView search_error;
    ImageView search_btn;
    ListView search_list;
    private DatabaseManager dbm;
    ArrayList<Set> sets;
    SearchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences))
            setTheme(R.style.AppThemeLight);
        else
            setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbm = new DatabaseManager(this);

        search_box = findViewById(R.id.search_box);
        search_error = findViewById(R.id.search_error);
        search_btn = findViewById(R.id.search_btn);
        search_list = findViewById(R.id.search_list);

        search_btn.setOnClickListener(this);

        search_box.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    searchDatabase();
                }
                return false;
            }
        });
    }

    public void searchDatabase(){
        if(search_box.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show();
            search_box.requestFocus();
        }
        else{
            sets = dbm.searchDatabase(search_box.getText().toString().trim(), SEARCH_SETS);
            if (sets != null){
               search_error.setVisibility(View.GONE);
               if (adapter != null){
                   adapter.replaceSet(sets);
                   adapter.notifyDataSetChanged();
               }
               else{
                   adapter = new SearchListAdapter(this, sets);
                   search_list.setAdapter(adapter);
               }
            }
            else if (adapter != null){
                adapter.clearSets();
                adapter.notifyDataSetChanged();
                search_error.setVisibility(View.VISIBLE);
            }
            else{
                search_error.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.search_btn):
                searchDatabase();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
