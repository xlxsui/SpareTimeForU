package com.sparetimeforu.android.sparetimeforu.fragment.module;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
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
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.adapter.LostAndFoundAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.Pagination;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.IdleThingDataBaseUtil;
import com.sparetimeforu.android.sparetimeforu.util.SearchThingBaseUtil;
import com.squareup.picasso.Picasso;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

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
    private int orgin = 0;
    private Activity activity;
    private String url;


    public RequestSearchThing(RequestSearchThingCallBack callBack, Activity activity) {
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
        url = STFUConfig.HOST + "/search_thing/refresh_newest";
        this.activity = activity;
    }

    @Override
    public void run() {
        FormBody formBody = new FormBody.Builder()
                .add("request_type", "寻物启事").build();
        //每次下拉加载都把orgin重置为0  即取数据库中最新的帖子信息
        orgin = 0;
        OkHttpUtil.sendOkHttpPostRequest(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> Toast.makeText(activity,
                        "无法获取任务信息，请检查网络是否正常", Toast.LENGTH_SHORT).show());
                mCallBack.fail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取数据成功
                HandleMessageUtil.handleSearchThingMessage(response.body().string());
                final List<SearchThing> searchThings = SearchThingBaseUtil.getSearchThing_data(orgin);
                if (searchThings.size() > 0) {
                    orgin += searchThings.size();
                }
                mCallBack.success(searchThings);
            }
        });
    }
}

public class SearchThingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LostAndFoundAdapter mAdapter;
    private SwipeRefreshLayout mSearchThingRefreshLayout;
    private List<SearchThing> mSearchThings;
    private Pagination mPagination;// 分页对象，加载更多时会用到
    private LoadingLayout loadingLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_thing_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_thing_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<SearchThing> list = new ArrayList<>();
        setupAdapter(list);

        loadingLayout=(LoadingLayout)view.findViewById(R.id.search_loading_layout);
        loadingLayout.setEmptyText("空空如也呢");
        loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                refresh();
            }
        });

        mSearchThingRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.search_thing_refresh_layout);
        mSearchThingRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        initRefreshLayout();
        Bundle args = getArguments();
        String query = args.getString("query", "not_query");
        if (query.equals("not_query")) {
            refresh();
        } else {
            search(query);
        }

        return view;
    }

    private void setupAdapter(List<SearchThing> searchThings) {
        mAdapter = new LostAndFoundAdapter(searchThings);

        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(true);

        //set main fragment header_errand
        View view = getLayoutInflater().inflate(R.layout.header_main_fragment,
                (ViewGroup) mRecyclerView.getParent(), false);
        ImageView img = (ImageView) view.findViewById(R.id.header_main_fragment);
        Picasso.get().load(R.drawable.header_search_thing)
                .resize(200, 200)
                .centerCrop()
                .into(img);
        mAdapter.addHeaderView(view);
        mAdapter.setOnLoadMoreListener(this::loadMore, mRecyclerView);

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
        mPagination = new Pagination();// 重来页数也要重置
        loadingLayout.setStatus(LoadingLayout.Loading);
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mSearchThingRefreshLayout.setRefreshing(true);
        new RequestSearchThing(new RequestSearchThingCallBack() {
            @Override
            public void success(List<SearchThing> data) {
                Snackbar.make(getView(), "Refresh finished! ", BaseTransientBottomBar.LENGTH_SHORT).show();

                //do something

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingLayout.setStatus(LoadingLayout.Success);
                        mAdapter.setNewData(data);//update data
                        mAdapter.setEnableLoadMore(true);
                        mSearchThingRefreshLayout.setRefreshing(false);
                        Snackbar.make(getView(), "Refresh finished! ", BaseTransientBottomBar.LENGTH_SHORT).show();
                        if (data.size() < 6) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                });
            }

            @Override
            public void fail(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getView(), "Network error! ", BaseTransientBottomBar.LENGTH_SHORT).show();
                        loadingLayout.setStatus(LoadingLayout.Error);
                        mAdapter.setEnableLoadMore(true);
                        mSearchThingRefreshLayout.setRefreshing(false);
                    }
                });

            }
        }, getActivity()).start();
    }

    private void search(String query) {
        mSearchThingRefreshLayout.setRefreshing(true);

        FormBody body = new FormBody.Builder()
                .add("content", query)
                .build();

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/search_thing/search", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(() -> {
                    mSearchThingRefreshLayout.setRefreshing(false);
                    mAdapter.setEnableLoadMore(false);// 防止可以上拉加载

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<SearchThing> searchThings = HandleMessageUtil
                        .handlePostSearchThingMessage(response.body().string());

                getActivity().runOnUiThread(() -> {
                    mSearchThingRefreshLayout.setRefreshing(false);
                    mAdapter.setNewData(searchThings);//update data
                    mAdapter.setEnableLoadMore(false);// 防止可以上拉加载

                });
            }
        });
    }

    /**
     * load more
     */
    public void loadMore() {
        FormBody body = new FormBody.Builder()
                .add("page", mPagination.getNext_num() + "")
                .build();

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/search_thing/load_more", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> {
                    mAdapter.loadMoreFail();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    status = jsonObject.getString("status");
                } catch (Exception e) {
                    Logger.e(e.toString());
                }
                if (status.equals("success")) {
                    getActivity().runOnUiThread(() -> {
                        List<SearchThing> searchThings = HandleMessageUtil
                                .handlePostSearchThingMessage(responseString);
                        mPagination = HandleMessageUtil
                                .handlePaginationMessage(responseString);
                        if (mPagination.getPage() == mPagination.getPages()) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                        if (searchThings != null) {
                            mAdapter.addData(searchThings);
                        }
                    });
                } else {
                    getActivity().runOnUiThread(() -> mAdapter.loadMoreFail());
                }
            }
        });
    }
}
