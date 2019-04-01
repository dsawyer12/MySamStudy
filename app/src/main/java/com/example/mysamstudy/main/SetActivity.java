package com.example.mysamstudy.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SetListAdapter;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SetActivity extends AppCompatActivity implements View.OnClickListener,
        ConfirmRemoveCardsDialogue.OnCardsReomved {
    private static final String TAG = "TAG";

    TextView set_title, header_title;
    ImageView set_edit, set_add, back_btn, header_exapansion;
    EditText new_card_question, new_card_answer;
    LinearLayout header;
    CardView new_card, no_cards;
    ListView list;
    ImageView delete_cards;
    InputMethodManager inputManager;
    SetListAdapter.OnCardClickListener listener;
    boolean is_delete_view = false;

    private SetListAdapter adapter;
    private Set set;
    private boolean createMode;

    @Override
    public void onRemove(boolean remove) {
        if (remove){
            DatabaseManager dbm = new DatabaseManager(this);
            for (int i = 0; i < adapter.getDelete_set().size(); i++){
                dbm.deleteCard(adapter.getDelete_set().get(i).getCardID());
            }
            set.setSetSize(set.getSetSize() - adapter.getDelete_set().size());
            dbm.updateSetSize(set);

            is_delete_view = false;
            delete_cards.setVisibility(View.GONE);
            set.getCards().removeAll(adapter.getDelete_set());
            adapter.updateSet();
            adapter.notifyDataSetChanged();

            if (set.getSetSize() != 0){
                header_title.setText(String.valueOf(set.getSetSize()) + " Card");
                header_title.setTextColor(ContextCompat.getColor(this, R.color.lightOrange));
            }
            else{
                header_title.setText("No Cards");
                header_title.setTextColor(ContextCompat.getColor(this, R.color.darkOrange));
                no_cards.setVisibility(View.VISIBLE);
            }
        }
    }

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
        setContentView(R.layout.activity_set);

        set_title = findViewById(R.id.set_title);
        set_edit = findViewById(R.id.set_edit);
        set_add = findViewById(R.id.set_add);
        back_btn = findViewById(R.id.back_btn);
        list = findViewById(R.id.listview);
        new_card = findViewById(R.id.new_card);
        no_cards = findViewById(R.id.cardview);
        delete_cards = findViewById(R.id.delete_cards);
        header = findViewById(R.id.card_list_header);
        header_title = findViewById(R.id.card_list_header_title);
        header_exapansion = findViewById(R.id.card_list_expansion);
        new_card_question = findViewById(R.id.card_question);
        new_card_answer = findViewById(R.id.card_answer);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        back_btn.setOnClickListener(this);
        set_edit.setOnClickListener(this);
        set_add.setOnClickListener(this);
        header.setOnClickListener(this);
        delete_cards.setOnClickListener(this);

        listener = new SetListAdapter.OnCardClickListener() {
            @Override
            public void onLongCLick(boolean in_delete_view) {
                if (in_delete_view){
                    delete_cards.setVisibility(View.VISIBLE);
                    is_delete_view = in_delete_view;
                    adapter.notifyDataSetChanged();
                }
                else{
                    delete_cards.setVisibility(View.GONE);
                    is_delete_view = in_delete_view;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onClick() {

            }
        };

        set = getIntent().getParcelableExtra("selectedSet");
        if (set != null){
            set_title.setText(set.getSetName());
//            SettingsManager.getSharedPreferences(this, SettingsManager.user_session);
//            Gson gson = new Gson();
//            String jobj =  SettingsManager.getUserSession(SettingsManager.user_session);
//            User user = gson.fromJson(jobj, User.class);
            setList();
        }

        new_card_question.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    new_card_answer.requestFocus();
                    inputManager.showSoftInput(new_card_answer, InputMethodManager.SHOW_IMPLICIT);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        new_card_answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }

    private void setList() {
        DatabaseManager dbm = new DatabaseManager(this);
        set.setCards(dbm.getCards(set.getSetId()));
        if (set.getSetSize() != 0){
            header_title.setText(String.valueOf(set.getSetSize()) + " Card");
            header_title.setTextColor(ContextCompat.getColor(this, R.color.lightOrange));
            adapter = new SetListAdapter(this, set, listener);
            list.setAdapter(adapter);
        }
        else{
            header_title.setText("No Cards");
            header_title.setTextColor(ContextCompat.getColor(this, R.color.darkOrange));
            no_cards.setVisibility(View.VISIBLE);
        }
    }

    private void addListItem(String question, String answer) {
        DatabaseManager dbm = new DatabaseManager(this);
        Card newCard = new Card(question, answer, set.getSetId());
        dbm.addCard(newCard);
        set.setSetSize(set.getSetSize()+1);
        dbm.updateSetSize(set);
        if (set.getCards() != null){
            set.getCards().add(newCard);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            reInitList();
        }
        else{
            ArrayList<Card> newSetCardList = new ArrayList<>();
            newSetCardList.add(newCard);
            set.setCards(newSetCardList);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            reInitList();
        }
    }

    public void reInitList(){
        if (adapter != null){
            createMode = false;
            adapter.notifyDataSetChanged();
            new_card.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            set_add.setImageResource(R.drawable.ic_add);
            header_exapansion.setImageResource(R.drawable.ic_collapse);
        }
        else{
            adapter = new SetListAdapter(this, set, listener);
            list.setAdapter(adapter);
            createMode = false;
            new_card.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            set_add.setImageResource(R.drawable.ic_add);
            header_exapansion.setImageResource(R.drawable.ic_collapse);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.set_edit):

                break;

            case(R.id.set_add):
                if (createMode){
                    String question = new_card_question.getText().toString().trim();
                    String answer = new_card_answer.getText().toString().trim();
                    if (!question.isEmpty() && !answer.isEmpty()){
                        new_card_question.getText().clear();
                        new_card_answer.getText().clear();
                        addListItem(question, answer);
                    }

                    if (question.isEmpty() && !answer.isEmpty()){
                        new_card_question.setError("Enter a Question");
                        new_card_question.requestFocus();
                        return;
                    }
                    if (answer.isEmpty() && !question.isEmpty()){
                        new_card_answer.setError("Enter an Answer");
                        new_card_answer.requestFocus();
                        return;
                    }
                    else{
                        createMode = false;
                        new_card.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                        set_add.setImageResource(R.drawable.ic_add);
                        header_exapansion.setImageResource(R.drawable.ic_collapse);
                        setList();
                    }
                }
                else{
                    createMode = true;
                    new_card.setVisibility(View.VISIBLE);
                    no_cards.setVisibility(View.GONE);
                    list.setVisibility(View.GONE);
                    set_add.setImageResource(R.drawable.ic_finish);
                    header_exapansion.setImageResource(R.drawable.ic_expand);
                    new_card_question.requestFocus();
                    inputManager.showSoftInput(new_card_question, InputMethodManager.SHOW_IMPLICIT);
                }
                break;

            case(R.id.card_list_header):
                if (list.isShown()){
                    list.setVisibility(View.GONE);
                    header_exapansion.setImageResource(R.drawable.ic_expand);
                }
                else{
                    list.setVisibility(View.VISIBLE);
                    header_exapansion.setImageResource(R.drawable.ic_collapse);
                }
                break;

            case(R.id.back_btn):
                Intent intent = new Intent(SetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case(R.id.delete_cards):
                if (adapter.getDelete_set().isEmpty()){
                    Toast.makeText(this, "Select the cards you wish to remove", Toast.LENGTH_SHORT).show();
                }
                else{
                    ConfirmRemoveCardsDialogue dialog = new ConfirmRemoveCardsDialogue();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("delete_set", adapter.getDelete_set());
                    dialog.setArguments(args);
                    dialog.show(getSupportFragmentManager(), "remove_sets_dialog");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (createMode){
            reInitList();
            return;
        }
        if (is_delete_view){
            is_delete_view = false;
            adapter.setIs_deleteView(false);
            delete_cards.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            return;
        }
        super.onBackPressed();
    }
}
