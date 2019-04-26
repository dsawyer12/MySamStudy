package com.example.mysamstudy.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.CardListRecyclerView;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SetListRecyclerView;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ACTIVITY_NUM = 1;

    TextView favorites_error;
    ImageView favorites_back_btn, favorites_refresh;
    private User user;

    RecyclerView recyclerView;
    SetListRecyclerView adapter;
    SetListRecyclerView.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences))
            setTheme(R.style.AppThemeLight);
        else
            setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favorites_error = findViewById(R.id.favorites_error);
        favorites_back_btn = findViewById(R.id.favorites_back_btn);
        recyclerView = findViewById(R.id.recyclerView);
        favorites_refresh = findViewById(R.id.favorites_refresh);
        favorites_back_btn.setOnClickListener(this);
        favorites_refresh.setOnClickListener(this);

        SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
        Gson gson = new Gson();
        String json = SettingsManager.getUserSession(SettingsManager.user_session);
        user = gson.fromJson(json, User.class);

        if (user != null)
            getUserFavorites();
        else{
            favorites_error.setVisibility(View.VISIBLE);
            favorites_error.setText("Could not retrieve user preferences");
        }
    }

    public void getUserFavorites(){
        ArrayList<Integer> keySet;
        ArrayList<Set> sets = new ArrayList<>();

        DatabaseManager dbm = new DatabaseManager(this);
        keySet = dbm.getFavoritesKeySet(user.getUser_id());
        if (keySet != null){
            for (int i = 0; i < keySet.size(); i++){
                sets.add(dbm.getSetById(keySet.get(i)));
            }
            listener = new SetListRecyclerView.OnItemClickListener() {
                @Override
                public void onSetStart(Set set) {
                    Intent intent = new Intent(FavoritesActivity.this, SetStartActivity.class);
                    intent.putExtra("mySet", set);
                    intent.putExtra("ACTIVITY_NUM", ACTIVITY_NUM);
                    startActivity(intent);
                }

                @Override
                public void onSetClick(Set set) {
                    viewSet(set);
                }

                @Override
                public void onSetLongClick(boolean delete_view) {}
            };
            adapter = new SetListRecyclerView(this, ACTIVITY_NUM, sets, listener);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else{
            favorites_error.setVisibility(View.VISIBLE);
            favorites_error.setText("Your favorites is empty");
        }
    }

    public void viewSet(Set set){
        SearchItemFragment item = new SearchItemFragment();
        Bundle args = new Bundle();
        args.putParcelable("item", set);
        item.setArguments(args);
        setFragment(item, "item");
    }

    public void setFragment(Fragment fragment, String tag){
        FragmentManager manager = this.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.favorites_frame, fragment, tag).addToBackStack(null).commit();
    }

    public void refresh(){
        Intent intent = new Intent(FavoritesActivity.this, FavoritesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("item") != null){
            getSupportFragmentManager().popBackStackImmediate();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.favorites_back_btn):
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            case(R.id.favorites_refresh):
                refresh();
                break;
        }
    }
}
