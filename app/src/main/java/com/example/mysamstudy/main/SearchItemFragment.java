package com.example.mysamstudy.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mysamstudy.R;
import com.example.mysamstudy.objects.Set;

public class SearchItemFragment extends Fragment {

    Set set;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        set = getSetFromBundle();
        if (set != null){
            Toast.makeText(getActivity(), set.getSetName(), Toast.LENGTH_SHORT).show();
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
}
