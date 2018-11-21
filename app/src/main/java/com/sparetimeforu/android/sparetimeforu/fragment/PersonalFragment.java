package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.user.User;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by HQY on 2018/11/15.
 */

public class PersonalFragment extends Fragment {

    public static String LoginServerUrl = " https://www.easy-mock.com/mock/5bebc836e0c6d321cade7458/success/LoginServer";

    //用户个人信息
    private TextView personal_favourable_rate, personal_nickname, personal_signate;
    private ImageView personal_avator;

    //用户界面需要相应的控件
    private TextView personal_mission_received, personal_mission_released;

    private User user;
    private Toolbar toolbar;
    private ImageView personal_go_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, container, false);

        //初始化控件
        personal_avator = (ImageView) view.findViewById(R.id.personal_avator);
        personal_nickname = (TextView) view.findViewById(R.id.personal_nickname);
        personal_signate = (TextView) view.findViewById(R.id.personal_signate);
        personal_mission_received = (TextView) view.findViewById(R.id.personal_mission_received);
        personal_mission_released = (TextView) view.findViewById(R.id.personal_mission_released);
        personal_favourable_rate = (TextView) view.findViewById(R.id.personal_favourable_rate);
        initDate();


        //设置Toolbar
        toolbar = view.findViewById(R.id.personal_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        //设置Toolbar中返回按钮的点击事件

        personal_go_back = (ImageView) view.findViewById(R.id.persnal_go_back);
        personal_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    /**
     * 获取用户信息
     */
    public void initDate() {
        //先用服务器，后续需要使用Activity间的信息传递
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        setUI();
    }

    private void setUI() {
        if (user != null) {
            personal_nickname.setText(user.getNick_name());
            personal_favourable_rate.setText(user.getFavourable_rate() + "%");
            personal_signate.setText(user.getSignature());
        }

    }


}
