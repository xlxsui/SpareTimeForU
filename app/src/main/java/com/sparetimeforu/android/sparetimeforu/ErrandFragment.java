package com.sparetimeforu.android.sparetimeforu;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandAdapter;
import com.sparetimeforu.android.sparetimeforu.recycler.view.item.Errand;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jin on 2018/11/3.
 */


/**
 * 跑腿的Fragment
 */
public class ErrandFragment extends Fragment {
    //have what, do what

    private RecyclerView mRecyclerView;
    private ErrandAdapter mAdapter;
    private List<Errand> mErrands;
    private SwipeRefreshLayout mErrandRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("Errand", "-----Create the errand fragment View.");
        View view = inflater.inflate(R.layout.fragment_errand_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_errand_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();

        /**
         *refresh Layout
         */
        mErrandRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.errand_reflash_layout);
        mErrandRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mErrandRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get data refresh UI
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mErrandRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        return view;
    }

    private void setupAdapter() {

        mAdapter = new ErrandAdapter();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(false);
        mRecyclerView.setAdapter(mAdapter);
    }

}





















