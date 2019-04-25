package com.example.mysamstudy.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;

public class NewSetFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TAG";

    DatabaseManager databaseManager;

    ImageView back_btn, finish;
    TextView set_title;
    EditText set_name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_set, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseManager = new DatabaseManager(getActivity());

        back_btn = view.findViewById(R.id.back_btn);
        finish = view.findViewById(R.id.finish);
        set_title = view.findViewById(R.id.set_title);
        set_name = view.findViewById(R.id.set_name);
        set_name.requestFocus();

        back_btn.setOnClickListener(this);
        finish.setOnClickListener(this);

        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(set_name, InputMethodManager.SHOW_IMPLICIT);

        set_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                set_title.setText(set_name.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        set_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    String setName = set_name.getText().toString().trim();
                    if (!setName.isEmpty()){
                        createSet(setName);
                    }
                    else{
                        set_name.setError("Enter a name");
                        set_name.requestFocus();
                    }
                }
                return false;
            }
        });
    }

    private void createSet(String setName) {
        SettingsManager.getSharedPreferences(getActivity(), SettingsManager.user_session);
        Gson gson = new Gson();
        String json = SettingsManager.getUserSession(SettingsManager.user_session);
        User user = gson.fromJson(json, User.class);

        Set set = new Set(setName, user.getUser_id());
        SettingsManager.getSharedPreferences(getActivity(), SettingsManager.share_all_sets_preferences);
        set.setShare(SettingsManager.get_share_all_sets_preferences(SettingsManager.share_all_sets_preferences));
        long result = databaseManager.addSet(set);
        if (result != 0){
            Log.d(TAG, "createSet: Success");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            Log.d(TAG, "createSet: UNSUCCESFUL");
            Toast.makeText(getActivity(), "Failed to create set", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.back_btn):
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;

            case(R.id.finish):
                String setName = set_name.getText().toString().trim();
                if (!setName.isEmpty()){
                    createSet(setName);
                }
                else{
                    set_name.setError("Enter a name");
                    set_name.requestFocus();
                    return;
                }
                break;
        }
    }
}
