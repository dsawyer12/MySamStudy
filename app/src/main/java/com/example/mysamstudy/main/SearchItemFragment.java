package com.example.mysamstudy.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;
import com.example.mysamstudy.utils.CardListRecyclerView;
import com.example.mysamstudy.utils.DatabaseManager;
import com.example.mysamstudy.utils.SettingsManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchItemFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "TAG";

    ImageView search_set_back, search_set_favorite;
    TextView search_set_name, search_set_size, no_cards;
    RecyclerView recyclerView;
    boolean isFavorite;

    User user;
    Set set;
    CardListRecyclerView adapter;
    CardListRecyclerView.OnCardClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search_set_back = view.findViewById(R.id.search_set_back);
        search_set_favorite = view.findViewById(R.id.search_set_favorite);
        search_set_name = view.findViewById(R.id.search_set_name);
        search_set_size = view.findViewById(R.id.search_set_size);
        no_cards = view.findViewById(R.id.no_cards);
        recyclerView = view.findViewById(R.id.recyclerView);

        search_set_back.setOnClickListener(this);
        search_set_favorite.setOnClickListener(this);

        SettingsManager.getSharedPreferences(getActivity(), SettingsManager.user_session);
        Gson gson = new Gson();
        String jobj = SettingsManager.getUserSession(SettingsManager.user_session);
        user = gson.fromJson(jobj, User.class);

        set = getSetFromBundle();
        if (set != null && user != null){
            search_set_name.setText(set.getSetName());
            search_set_size.setText(String.valueOf(set.getSetSize()) + " Card(s)");
            populateCards();
        }
        else{
            Toast.makeText(getActivity(), "Could not retrieve user preferences", Toast.LENGTH_SHORT).show();
            search_set_favorite.setEnabled(false);
        }
    }

    public void populateCards(){
        DatabaseManager dbm = new DatabaseManager(getActivity());
        isFavorite = dbm.getSetFavorite(user.getUser_id(), set.getSetId());
        if(isFavorite)
           search_set_favorite.setColorFilter(getResources().getColor(R.color.darkBlue));

        set.setCards(dbm.getCards(set.getSetId()));
        if (set.getSetSize() != 0){
            adapter = new CardListRecyclerView(getActivity(), set, listener);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setClickable(false);
        }
        else{
            no_cards.setVisibility(View.VISIBLE);
        }
    }

    public Set getSetFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelable("item");
        }
        else{
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.search_set_back):
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;

            case(R.id.search_set_favorite):
                if(isFavorite){
                    isFavorite = false;
                    search_set_favorite.setColorFilter(getResources().getColor(R.color.lightGrey));
                    DatabaseManager dbm = new DatabaseManager(getActivity());
                    dbm.removeSetFavorite(user.getUser_id(), set.getSetId());
                }
                else{
                    isFavorite = true;
                    search_set_favorite.setColorFilter(getResources().getColor(R.color.darkBlue));
                    DatabaseManager dbm = new DatabaseManager(getActivity());
                    dbm.addSetFavorite(user.getUser_id(), set.getSetId());
                }
                break;
        }
    }
}


















