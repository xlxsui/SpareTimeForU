package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.adapter.FriendAdapter;

import java.util.List;

import butterknife.BindView;
import cn.jpush.android.service.JCommonService;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by HQY on 2019/7/20.
 */

public class FriendFragment extends Fragment {
    @Nullable
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    public ImageView imageView;
    public RecyclerView friend_recyclerview;//朋友列表
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        JMessageClient.init(getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_friend,container,false);
        friend_recyclerview=(RecyclerView)view.findViewById(R.id.friend_recyclerView);
        friend_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        imageView=(ImageView)view.findViewById(R.id.menu_back_icon);
        imageView.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {getActivity().finish();}});
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                if(i==0){//获取朋友列表成功
                    friend_recyclerview.setAdapter(new FriendAdapter(list,getActivity()));
                }else{
                    Snackbar.make(getView(),"网络请求错误，请检查您的设备",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static FriendFragment newInstance(){
        return new FriendFragment();
    }
}
