package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandReceivedAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;

import java.util.List;

/**
 * Created by HQY on 2018/12/2.
 */

public class ErrandContentFragment extends Fragment{
    private View view;
    private ErrandReceivedAdapter mAdapter;
    private List<Errand> data;
    private RecyclerView mRecyclerView;
    int mode;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.mission_received_main,container,false);


        mRecyclerView=(RecyclerView)view.findViewById(R.id.mission_received_mission_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //获取data数据
        initDate();
        setupAdapter();

        return view;
    }


    /**
     * get Errand data
     */
    private void initDate(){
        Bundle bundle=getArguments();
        mode=bundle.getInt("mode");

        if(mode==Errand_received_fragment.RECEIVED_MISSION_DOING)
        data= DataServer.getErrandData(10);
        else data=DataServer.getErrandData(10);
    }
    private void setupAdapter(){
        mAdapter=new ErrandReceivedAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }
}
