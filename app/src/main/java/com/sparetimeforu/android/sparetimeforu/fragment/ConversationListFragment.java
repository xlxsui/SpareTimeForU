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

import java.util.List;

import butterknife.BindView;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by HQY on 2019/7/20.
 */

public class ConversationListFragment extends Fragment {
    RecyclerView mRecyclerView;
    ImageView menu_back;
    List<Conversation> conversations;
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        JMessageClient.init(getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_friend,container,false);//可以和好友界面公用一个布局，只是recyclerView中的item布局不同
        mRecyclerView=(RecyclerView)view.findViewById(R.id.friend_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        init_data();
        if(STFUConfig.globalEventListener!=null){
            STFUConfig.globalEventListener.setConversationListFragment(this);
            JMessageClient.registerEventReceiver(STFUConfig.globalEventListener);
        }
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        menu_back=(ImageView)view.findViewById(R.id.menu_back_icon);
        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STFUConfig.globalEventListener.setConversationListFragment(null);
                getActivity().finish();
            }
        });
        return view;
    }

    public void init_data(){
        //初始化，获取所有conversation
        conversations=JMessageClient.getConversationList();
        mRecyclerView.setAdapter(new ConversationListAdapter(conversations,getContext()));
    }

    public static ConversationListFragment newInstance() {
        return new ConversationListFragment();
    }
}
