package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.adapter.ErrandAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.LostAndFoundAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.StudyAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by HQY on 2019/10/7.
 */


public class PostReleasedFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private BottomNavigationView mission_received_switch;
    private LinearLayout mLinearLayout;
    private FragmentManager mFragmentManager;
    private ErrandContentFragment mErrandFragment;
    private RecyclerView recyclerView;
    private int user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.post_released, container, false);

        user_id=STFUConfig.sUser.getUser_id();
//        user_id = 1;
        mFragmentManager = getFragmentManager();

        recyclerView = (RecyclerView) view.findViewById(R.id.post_released_recylerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置toolbar
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //设置导航切换界面
        mission_received_switch = (BottomNavigationView) view.findViewById(R.id.post_released_select);
        mission_received_switch.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.post_released_errand:
                        load_Errand_Recylerview();
                        return true;
                    case R.id.post_released_find:
                        load_Find_Recylerview();
                        return true;
                    case R.id.post_released_study:
                        load_Study_Recylerview();
                        return true;
                    case R.id.post_released_trade:
                        load_Trade_Recylerview();
                        return true;
                }
                return false;
            }
        });
        load_Errand_Recylerview();
        return view;
    }

    String url = STFUConfig.HOST + "/mission";

    private void load_Errand_Recylerview() {
        //获取服务器数据
        FormBody formBody = new FormBody.Builder()
                .add("user_id", user_id + "").build();
        OkHttpUtil.sendOkHttpPostRequest(url + "/get_user_posts", formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getView(), "网络请求错误", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Errand> data;
                data = HandleMessageUtil.handleReleasedErrandMessage(response.body().string());
                ErrandAdapter adapter = new ErrandAdapter(data, getActivity());
                getActivity().runOnUiThread(() -> recyclerView.setAdapter(adapter));
            }
        });

    }

    private void load_Find_Recylerview() {
        //获取服务器数据
        FormBody formBody = new FormBody.Builder()
                .add("user_id", user_id + "").build();
        OkHttpUtil.sendOkHttpPostRequest(url + "/getReleasedFind", formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getView(), "网络请求错误", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<SearchThing> data;
                data = HandleMessageUtil.handlePostSearchThingMessage(response.body().string());
                LostAndFoundAdapter adapter = new LostAndFoundAdapter(data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });

    }

    private void load_Study_Recylerview() {
        //获取服务器数据
        FormBody formBody = new FormBody.Builder()
                .add("user_id", user_id + "").build();
        OkHttpUtil.sendOkHttpPostRequest(url + "/getReleasedStudy", formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getView(), "网络请求错误", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Study> data;
                data = HandleMessageUtil.handlePostStudyMessage(response.body().string());
                StudyAdapter adapter = new StudyAdapter(data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void load_Trade_Recylerview() {
        //获取服务器数据
        FormBody formBody = new FormBody.Builder()
                .add("user_id", user_id + "").build();
        OkHttpUtil.sendOkHttpPostRequest(url + "/getReleasedTrade", formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getView(), "网络请求错误", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<IdleThing> data;
                data = HandleMessageUtil.handlePostIdleThingMessage(response.body().string());
                IdleThingAdapter adapter = new IdleThingAdapter(data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }
}