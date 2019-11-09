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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.Pagination;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.IdleThingDataBaseUtil;
import com.squareup.picasso.Picasso;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/14.
 */

interface RequestIdleThingCallBack {
    void success(List<IdleThing> data);

    void fail(Exception e);
}

class RequestIdleThing extends Thread {
    private RequestIdleThingCallBack mCallBack;
    private Handler mHandler;
    private String url;
    private Activity activity;
    private int origin = 0;


    public RequestIdleThing(RequestIdleThingCallBack callBack, Activity activity) {
        url = STFUConfig.HOST + "/idle_thing/refresh_newest";
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
        this.activity = activity;
    }

    @Override
    public void run() {
        FormBody formBody = new FormBody.Builder()
                .add("request_type", "二手交易").build();
        //每次下拉加载都把orgin重置为0  即取数据库中最新的帖子信息
        origin = 0;
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
                HandleMessageUtil.handleIdleThingMessage(response.body().string());
                final List<IdleThing> idleThings = IdleThingDataBaseUtil.getIdleThing_data(origin);
                if (idleThings.size() > 0) {
                    origin += idleThings.size();
                }
                mCallBack.success(idleThings);
            }
        });
    }
}



public class IdleThingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private IdleThingAdapter mAdapter;
    private SwipeRefreshLayout mIdleThingRefreshLayout;
    private Pagination mPagination;// 分页对象，加载更多时会用到
    private LoadingLayout loadingLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_idle_thing_main, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_idle_thing_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter(DataServer.getIdleThingData(0));

        loadingLayout=(LoadingLayout)view.findViewById(R.id.idle_loading_layout);
        loadingLayout.setEmptyText("空空如也呢，快来发布闲置物品信息吧");
        loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                refresh();
            }
        });
        mIdleThingRefreshLayout = view.findViewById(R.id.idle_thing_refresh_layout);
        mIdleThingRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
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

    private void setupAdapter(List<IdleThing> idleThingData) {
        mAdapter = new IdleThingAdapter(idleThingData);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(true);

        //set main fragment header_errand
        View view = getLayoutInflater().inflate(R.layout.header_main_fragment,
                (ViewGroup) mRecyclerView.getParent(), false);
        ImageView img = (ImageView) view.findViewById(R.id.header_main_fragment);
        Picasso.get().load(R.drawable.header_idle_thing)
                .resize(200, 200)
                .centerCrop()
                .into(img);
        mAdapter.addHeaderView(view);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this::loadMore, mRecyclerView);


        mRecyclerView.setAdapter(mAdapter);
    }


    private void initRefreshLayout() {
        mIdleThingRefreshLayout.setOnRefreshListener(this::refresh);
    }

    /**
     * refresh new post
     */
    private void refresh() {
        loadingLayout.setStatus(LoadingLayout.Loading);
        mPagination = new Pagination();// 重来页数也要重置
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mIdleThingRefreshLayout.setRefreshing(true);
        new RequestIdleThing(new RequestIdleThingCallBack() {
            @Override
            public void success(List<IdleThing> data) {
                Snackbar.make(getView(), "Refresh finished! ",
                        BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                //do something
                getActivity().runOnUiThread(() -> {
                    loadingLayout.setStatus(LoadingLayout.Success);
                    mAdapter.setNewData(data);//update data
                    if(data.size()==0) loadingLayout.setStatus(LoadingLayout.Empty);
                    mAdapter.setEnableLoadMore(true);
                    mIdleThingRefreshLayout.setRefreshing(false);
                    Snackbar.make(getView(), "Refresh finished! ",
                            BaseTransientBottomBar.LENGTH_SHORT).show();
                    if (data.size() < 6) {
                        mAdapter.loadMoreEnd();
                    }
                });
            }

            @Override
            public void fail(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getView(), "Network error! ",
                                BaseTransientBottomBar.LENGTH_SHORT).show();
                        loadingLayout.setStatus(LoadingLayout.Error);
                        mAdapter.setEnableLoadMore(true);
                        mIdleThingRefreshLayout.setRefreshing(false);
                    }
                });

            }
        }, getActivity()).start();
    }

    /**
     * search post
     *
     * @param query the content be searched
     */
    private void search(String query) {
        mIdleThingRefreshLayout.setRefreshing(true);

        FormBody body = new FormBody.Builder()
                .add("content", query)
                .build();

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/idle_thing/search",
                body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(() -> {
                            mIdleThingRefreshLayout.setRefreshing(false);
                            mAdapter.setEnableLoadMore(false);// 搜索之后不能下拉加载
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        List<IdleThing> idleThings = HandleMessageUtil
                                .handlePostIdleThingMessage(response.body().string());

                        getActivity().runOnUiThread(() -> {
                            mIdleThingRefreshLayout.setRefreshing(false);
                            mAdapter.setNewData(idleThings);// update data
                            mAdapter.setEnableLoadMore(false);// 搜索之后不能下拉加载
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

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/idle_thing/load_more",
                body, new Callback() {
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

                                List<IdleThing> idleThings = HandleMessageUtil
                                        .handlePostIdleThingMessage(responseString);
                                mPagination = HandleMessageUtil
                                        .handlePaginationMessage(responseString);
                                if (mPagination.getPage() == mPagination.getPages()) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
                                }

                                if (idleThings != null) {
                                    mAdapter.addData(idleThings);
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(() -> mAdapter.loadMoreFail());
                        }
                    }
                });
    }

}
