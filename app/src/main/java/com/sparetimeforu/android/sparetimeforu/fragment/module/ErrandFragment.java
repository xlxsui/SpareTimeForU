package com.sparetimeforu.android.sparetimeforu.fragment.module;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/3.
 */


interface RequestErrandCallBack {
    void success(List<Errand> data);

    void fail(Exception e);
}

class RequestErrand extends Thread {
    private RequestErrandCallBack mCallBack;
    private Handler mHandler;


    public RequestErrand(RequestErrandCallBack callBack) {
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
                        mCallBack.success(DataServer.getErrandData(15));
                    }
                });
                break;
            default:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getErrandData(15));
                    }
                });
                break;

        }
    }
}


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

        Logger.d("Create the errand fragment View.");
        View view = inflater.inflate(R.layout.fragment_errand_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_errand_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter(DataServer.getErrandData(10));

        mErrandRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.errand_refresh_layout);
        mErrandRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        initRefreshLayout();

        return view;
    }

    private void setupAdapter(List<Errand> errands) {

        mAdapter = new ErrandAdapter(errands);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(false);

        //set main fragment header_errand
        View view = getLayoutInflater().inflate(R.layout.header_main_fragment,
                (ViewGroup) mRecyclerView.getParent(), false);
        ImageView img = (ImageView) view.findViewById(R.id.header_main_fragment);
        Picasso.get().load(R.drawable.header_errand)
                .resize(1080, 512)
                .centerCrop()
                .into(img);
        mAdapter.addHeaderView(view);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mErrandRefreshLayout.setOnRefreshListener(() -> refresh());
    }

    private void refresh() {
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        new RequestErrand(new RequestErrandCallBack() {
            @Override
            public void success(List<Errand> data) {
                Toast.makeText(getActivity(), "Refresh finished! ", Toast.LENGTH_SHORT).show();
                //do something
                setupAdapter(data);

                mAdapter.setEnableLoadMore(true);
                mErrandRefreshLayout.setRefreshing(false);
            }

            @Override
            public void fail(Exception e) {
                Toast.makeText(getActivity(), "Network error! ", Toast.LENGTH_SHORT).show();

                mAdapter.setEnableLoadMore(true);
                mErrandRefreshLayout.setRefreshing(false);
            }
        }).start();
    }


}





















