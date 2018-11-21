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
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.adapter.StudyAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.Study;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 */

interface RequestStudyCallBack {
    void success(List<Study> data);

    void fail(Exception e);
}

class RequestStudy extends Thread {
    private RequestStudyCallBack mCallBack;
    private Handler mHandler;


    public RequestStudy(RequestStudyCallBack callBack) {
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
                        mCallBack.success(DataServer.getStudyData(15));
                    }
                });
                break;
            default:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getStudyData(15));
                    }
                });
                break;

        }
    }
}

public class StudyFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private StudyAdapter mAdapter;
    private List<Study> mStudies;
    private SwipeRefreshLayout mStudyRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_study_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter(DataServer.getStudyData(15));

        mStudyRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.study_refresh_layout);
        mStudyRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        initRefreshLayout();

        return view;
    }

    private void setupAdapter(List<Study> studies) {
        mAdapter = new StudyAdapter(studies);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(false);

        //set main fragment header_errand
        View view = getLayoutInflater().inflate(R.layout.header_main_fragment,
                (ViewGroup) mRecyclerView.getParent(), false);
        ImageView img = (ImageView) view.findViewById(R.id.header_main_fragment);
        Picasso.get().load(R.drawable.header_study)
                .resize(1080, 512)
                .centerCrop()
                .into(img);
        mAdapter.addHeaderView(view);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mStudyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        new RequestStudy(new RequestStudyCallBack() {
            @Override
            public void success(List<Study> data) {
                Toast.makeText(getActivity(), "Refresh finished! ", Toast.LENGTH_SHORT).show();
                //do something
                setupAdapter(data);

                mAdapter.setEnableLoadMore(true);
                mStudyRefreshLayout.setRefreshing(false);
            }

            @Override
            public void fail(Exception e) {
                Toast.makeText(getActivity(), "Network error! ", Toast.LENGTH_SHORT).show();

                mAdapter.setEnableLoadMore(true);
                mStudyRefreshLayout.setRefreshing(false);
            }
        }).start();
    }

}

































