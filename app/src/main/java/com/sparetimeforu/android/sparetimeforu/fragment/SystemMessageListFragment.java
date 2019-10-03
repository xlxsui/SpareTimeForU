package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.adapter.ConversationListAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.SystemMessageListAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 *
 * 系统通知页面的fragment
 * Created by HQY on 2019/10/2.
 */

public class SystemMessageListFragment extends Fragment {

    @Nullable
    RecyclerView mRecyclerView;
    ImageView menu_back;
    List<SystemMessage> messages;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        JMessageClient.init(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_friend, container, false);//可以和好友界面公用一个布局，只是recyclerView中的item布局不同
        mRecyclerView = (RecyclerView) view.findViewById(R.id.friend_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        init_data();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        menu_back = (ImageView) view.findViewById(R.id.menu_back_icon);
        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    private void init_data() {
        if(STFUConfig.systemMessages!=null){
            messages=STFUConfig.systemMessages;
        }
        setAdapter(messages);
    }

    public void setAdapter(List<SystemMessage> messages){
        mRecyclerView.setAdapter(new SystemMessageListAdapter(messages,getActivity()));
    }

    public static SystemMessageListFragment newInstance() {
        return new SystemMessageListFragment();
    }
}
