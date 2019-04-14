package com.example.mysamstudy.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
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
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SetListAdapter;
import com.example.mysamstudy.utils.SettingsManager;

import java.util.ArrayList;

public class SetActivity extends AppCompatActivity implements View.OnClickListener,
        ConfirmRemoveCardsDialogue.OnCardsReomved, FragmentManager.OnBackStackChangedListener {
    private static final String TAG = "TAG";

    boolean is_delete_view = false, createMode = false;
    LinearLayout root_view, header;
    TextView set_title, header_title, edit_toolbar_title;
    ImageView set_edit, set_add, back_btn, header_exapansion, new_card_back, new_card_finish, delete_cards;
    EditText new_card_question, new_card_answer;
    CardView new_card, no_cards;
    ListView list;
    View editToolbar;
    Toolbar toolbar;
    Set set;
    InputMethodManager inputManager;
    SetListAdapter.OnCardClickListener listener;
    SetListAdapter adapter;

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
            set.getCards().removeAll(adapter.getDelete_set());
            delete_cards.setVisibility(View.GONE);
            adapter.updateSet();
            reInitList();

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

        root_view = findViewById(R.id.set_root_view);
        toolbar = findViewById(R.id.toolbar);
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
        edit_toolbar_title = findViewById(R.id.edit_card_toolbar_title);
        header_exapansion = findViewById(R.id.card_list_expansion);
        new_card_question = findViewById(R.id.card_question);
        new_card_question.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        new_card_question.setRawInputType(InputType.TYPE_CLASS_TEXT);
        new_card_answer = findViewById(R.id.card_answer);
        new_card_answer.setImeOptions(EditorInfo.IME_ACTION_DONE);
        new_card_answer.setRawInputType(InputType.TYPE_CLASS_TEXT);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        back_btn.setOnClickListener(this);
        set_edit.setOnClickListener(this);
        set_add.setOnClickListener(this);
        header.setOnClickListener(this);
        delete_cards.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

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
            public void onClick(Card card) {
                EditCardFragment editCardFragment = new EditCardFragment();
                Bundle args = new Bundle();
                args.putParcelable("editCard", card);
                editCardFragment.setArguments(args);
                setFragment(editCardFragment, "editCard");
            }
        };

        set = getIntent().getParcelableExtra("selectedSet");
        if (set != null){
            set_title.setText(set.getSetName());
            setList();
        }
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

    private void addListItem(String question, String answer){
        DatabaseManager dbm = new DatabaseManager(this);
        Card newCard = new Card(question, answer, set.getSetId());
        long success = dbm.addCard(newCard);
        if(success < 0){
            Toast.makeText(this, "Failed to add card", Toast.LENGTH_SHORT).show();
        }
        else{
            set.setSetSize(set.getSetSize()+1);
            dbm.updateSetSize(set);
            header_title.setText(String.valueOf(set.getSetSize()) + " Card");
            header_title.setTextColor(ContextCompat.getColor(this, R.color.lightOrange));

            if (set.getCards() != null){
                set.getCards().add(newCard);
                new_card_question.setText(null);
                new_card_answer.setText(null);
                new_card_question.requestFocus();
                reInitList();
            }
            else{
                ArrayList<Card> newSetCardList = new ArrayList<>();
                newSetCardList.add(newCard);
                set.setCards(newSetCardList);
                new_card_question.setText(null);
                new_card_answer.setText(null);
                new_card_question.requestFocus();
                reInitList();
            }
        }
    }

    public void reInitList(){
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
        else{
            adapter = new SetListAdapter(this, set, listener);
            list.setAdapter(adapter);
        }
    }

    public void exitCreateMode(){
        createMode = false;
        root_view.removeView(editToolbar);
        toolbar.setVisibility(View.VISIBLE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        new_card.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
        header_exapansion.setImageResource(R.drawable.ic_collapse);
        new_card_question.setText(null);
        new_card_answer.setText(null);
//        setList();
        reInitList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.set_edit):

                break;

            case(R.id.set_add):
                createMode = true;
                toolbar.setVisibility(View.GONE);
                editToolbar = getLayoutInflater().inflate(R.layout.layout_edit_action_bar, null);
                root_view.addView(editToolbar, 0);
                new_card_back = findViewById(R.id.new_card_back);
                new_card_finish = findViewById(R.id.new_card_finish);
                new_card_back.setOnClickListener(this);
                new_card_finish.setOnClickListener(this);

                new_card.setVisibility(View.VISIBLE);
                no_cards.setVisibility(View.GONE);
                new_card_question.requestFocus();
                inputManager.showSoftInput(new_card_question, InputMethodManager.SHOW_IMPLICIT);
                break;

            case(R.id.new_card_finish):
                if (!new_card_question.getText().toString().trim().isEmpty() &&
                            !new_card_answer.getText().toString().trim().isEmpty()){
                        addListItem(new_card_question.getText().toString().trim(),
                                new_card_answer.getText().toString().trim());
                    }

                    if (new_card_question.getText().toString().trim().isEmpty() &&
                            !new_card_answer.getText().toString().trim().isEmpty()){
                        new_card_question.setError("Enter a Question");
                        new_card_question.requestFocus();
                        return;
                    }
                    if (new_card_answer.getText().toString().trim().isEmpty() &&
                            !new_card_question.getText().toString().trim().isEmpty()){
                        new_card_answer.setError("Enter an Answer");
                        new_card_answer.requestFocus();
                        return;
                    }
//                    else{
//                        Log.d(TAG, "both are empty");
//                        new_card_question.setText(null);
//                        new_card_answer.setText(null);
//                    }
                break;

            case(R.id.new_card_back):
               exitCreateMode();
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
                onBackPressed();
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

    public void setFragment(Fragment fragment, String tag){
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame, fragment, tag).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (createMode){
           exitCreateMode();
           return;
        }
        if (is_delete_view){
            is_delete_view = false;
            adapter.setIs_deleteView(false);
            delete_cards.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            return;
        }
        if (getSupportFragmentManager().findFragmentByTag("editCard") != null){
            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("editCard")).commit();
            getSupportFragmentManager().popBackStack();
            return;
        }

        Intent intent = new Intent(SetActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackStackChanged() {
        setList();
    }
}
