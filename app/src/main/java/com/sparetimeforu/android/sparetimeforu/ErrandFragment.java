package com.sparetimeforu.android.sparetimeforu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jin on 2018/11/3.
 */


/**
 * 跑腿的Fragment
 */
public class ErrandFragment extends Fragment {
    //have what, do what

    private RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_errand_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_errand_main_recycler_view);

        return view;
    }



}
