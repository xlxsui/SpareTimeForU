package com.sparetimeforu.android.sparetimeforu.fragment.module;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.adapter.LostAndFoundAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 */

interface RequestSearchThingCallBack {
    void success(List<SearchThing> data);

    void fail(Exception e);
}

class RequestSearchThing extends Thread {
    private RequestSearchThingCallBack mCallBack;
    private Handler mHandler;


    public RequestSearchThing(RequestSearchThingCallBack callBack) {
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        Random random = new Random();
        int i = random.nextInt(5);
        switch (i) {
            case 0:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.fail(new RuntimeException("fail"));
                    }
                });
                break;
            case 1:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getSearchThingData(15));
                    }
                });
                break;
            default:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getSearchThingData(15));
                    }
                });
                break;

        }
    }
}

public class SearchThingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LostAndFoundAdapter mAdapter;
    private SwipeRefreshLayout mSearchThingRefreshLayout;
    private List<SearchThing> mSearchThings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_thing_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_thing_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter(DataServer.getSearchThingData(20));

        mSearchThingRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.search_thing_refresh_layout);
        mSearchThingRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        initRefreshLayout();

        return view;
    }

    private void setupAdapter(List<SearchThing> searchThings) {
        mAdapter = new LostAndFoundAdapter(searchThings);

        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(false);

        //set main fragment header_errand
        View view = getLayoutInflater().inflate(R.layout.header_main_fragment,
                (ViewGroup) mRecyclerView.getParent(), false);
        ImageView img = (ImageView) view.findViewById(R.id.header_main_fragment);
        Picasso.get().load(R.drawable.header_search_thing)
                .resize(200, 200)
                .centerCrop()
                .into(img);
        mAdapter.addHeaderView(view);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mSearchThingRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        new RequestSearchThing(new RequestSearchThingCallBack() {
            @Override
            public void success(List<SearchThing> data) {
                Snackbar.make(getView(), "Refresh finished! ", Snackbar.LENGTH_SHORT).show();

                //do something
                setupAdapter(data);

                mAdapter.setEnableLoadMore(true);
                mSearchThingRefreshLayout.setRefreshing(false);
            }

            @Override
            public void fail(Exception e) {
                Snackbar.make(getView(), "Network error! ", Snackbar.LENGTH_SHORT).show();

                mAdapter.setEnableLoadMore(true);
                mSearchThingRefreshLayout.setRefreshing(false);
            }
        }).start();
    }
}














