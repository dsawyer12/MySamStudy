package com.example.mysamstudy.main;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.CardListAdapter;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TAG";

    DatabaseManager databaseManager;
    private User user;

    DrawerLayout drawer;
    ListView listview;
    CardView cardview;
    Button log_out;
    ImageView add_new, delete_set;

    ArrayList<Set> sets;

    CardListAdapter adapter;
    CardListAdapter.OnItemLongClickListener listener;
    boolean is_delete_view = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        SettingsManager.getSharedPreferences(this, SettingsManager.dark_theme_preferences);
        if (!SettingsManager.getDarkTheme(SettingsManager.dark_theme_preferences)){
            setTheme(R.style.AppThemeLight);
        }
        else {
            setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = new DatabaseManager(this);
        SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
        Gson gson = new Gson();
        String jobj = SettingsManager.getUserSession(SettingsManager.user_session);
        user = gson.fromJson(jobj, User.class);

        listview = findViewById(R.id.listview);
        cardview = findViewById(R.id.cardview);
        add_new = findViewById(R.id.add_new);
        delete_set = findViewById(R.id.delete_sets);
        log_out = findViewById(R.id.logout);

        log_out.setOnClickListener(this);
        add_new.setOnClickListener(this);
        delete_set.setOnClickListener(this);

        setUpNavigationDrawer();

        listener = new CardListAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(boolean in_deleteView) {
                if (in_deleteView){
                    is_delete_view = in_deleteView;
                    delete_set.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                else{
                    is_delete_view = in_deleteView;
                    delete_set.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onClick() {
                Log.d(TAG, "onClick: ");
            }

            @Override
            public void onClick(Set set) {
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                intent.putExtra("selectedSet", set);
                startActivity(intent);
            }
        };

        getData();
    }

    public void setUpNavigationDrawer(){
        NavigationView navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        View header = navView.getHeaderView(0);
        header.setOnClickListener(this);
    }

    public void getData(){
        sets = databaseManager.getSets(user.getUser_id());
        if (sets != null){

            for (int i = 0; i < sets.size(); i++){
                Log.d(TAG, String.valueOf(sets.get(i).getSetSize()));
            }

            adapter = new CardListAdapter(this, sets, listener);
            listview.setAdapter(adapter);
        }
        else{
            Log.d(TAG, "no sets");
            cardview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (is_delete_view){
            is_delete_view = false;
            adapter.setIs_deleteView(false);
            delete_set.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.add_new):
                NewSetFragment newSet = new NewSetFragment();
                setFragment(newSet);
                break;
            case(R.id.logout):
                SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
                SettingsManager.logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case(R.id.delete_sets):
                if(adapter.getDelete_set().isEmpty()){
                    Toast.makeText(this, "Select the sets you wish to remove", Toast.LENGTH_SHORT).show();
                }
                else{
                    ConfirmRemoveSetsDialogue dialog = new ConfirmRemoveSetsDialogue();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("delete_set", adapter.getDelete_set());
                    dialog.setArguments(args);
                    dialog.show(getSupportFragmentManager(), "remove_sets_dialog");
                }
                break;
            case(R.id.drawer_header):
                Uri uri = Uri.parse("https://shsu.edu/");

                Intent link = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(link);
                break;
        }
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case(R.id.settings):
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            case(R.id.account):
                Intent intent1 = new Intent(MainActivity.this, AccountActivity.class);
                intent1.putParcelableArrayListExtra("sets", sets);
                startActivity(intent1);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.START);
        return true;
    }
}


