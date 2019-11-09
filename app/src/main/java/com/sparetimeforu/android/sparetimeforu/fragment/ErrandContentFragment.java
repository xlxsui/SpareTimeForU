package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandReceivedAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by HQY on 2018/12/2.
 */

public class ErrandContentFragment extends Fragment {
    private View view;
    private ErrandAdapter mAdapter;
    private List<Errand> data;
    private RecyclerView mRecyclerView;
    int mode;
    private String url = STFUConfig.HOST + "/mission";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mission_received_main, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.mission_received_mission_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //获取data数据
        initDate();

        return view;
    }

    /**
     * get Errand data
     */
    private void initDate() {
        Bundle bundle = getArguments();
        mode = bundle.getInt("mode");

        if (mode == Errand_received_fragment.RECEIVED_MISSION_DOING) {
            if (STFUConfig.sUser == null) return;
            FormBody formBody = new FormBody.Builder()
                    .add("user_id", STFUConfig.sUser.getUser_id() + "").build();
            OkHttpUtil.sendOkHttpPostRequest(url + "/get_user_received_ndone_posts", formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    List<Errand> data;
                    data = HandleMessageUtil.handleReleasedErrandMessage(response.body().string());
                    mAdapter = new ErrandAdapter(data, getActivity());
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(() -> setupAdapter());
                }
            });
        } else {
            FormBody formBody = new FormBody.Builder()
                    .add("user_id", STFUConfig.sUser.getUser_id() + "").build();
            OkHttpUtil.sendOkHttpPostRequest(url + "/get_user_received_done_posts", formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(() -> Snackbar.make(getView(), "网络请求错误", BaseTransientBottomBar.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    List<Errand> data;
                    data = HandleMessageUtil.handleReleasedErrandMessage(response.body().string());
                    mAdapter = new ErrandAdapter(data, getActivity());
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> setupAdapter());

                }
            });
        }
    }

    private void setupAdapter() {
        mRecyclerView.setAdapter(mAdapter);
    }
}
