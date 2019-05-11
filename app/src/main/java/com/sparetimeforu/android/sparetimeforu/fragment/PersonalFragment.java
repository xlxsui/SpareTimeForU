package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.EditActivity;
import com.sparetimeforu.android.sparetimeforu.activity.ErrandReceivedActivity;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.squareup.picasso.Picasso;

/**
 * Created by HQY on 2018/11/15.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener {
    //用户个人信息
    private TextView personal_favourable_rate, personal_nickname, personal_signate;
    private ImageView personal_avator;

    //用户界面需要相应的控件
    private TextView personal_mission_received, personal_mission_released;

    private Toolbar toolbar;
    private ImageView personal_go_back;
    private TextView personal_edit;//点击编辑按钮

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
        personal_edit = (TextView) view.findViewById(R.id.personal_edit);
        initDate();


        //给TextView添加监听

        personal_edit.setOnClickListener(this);
        personal_mission_received.setOnClickListener(this);
        personal_mission_released.setOnClickListener(this);


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
        STFUConfig.sUser = (User) intent.getSerializableExtra("user");
        setUI();
    }

    private void setUI() {
        if (STFUConfig.sUser != null) {
            personal_nickname.setText(STFUConfig.sUser.getNickname());
            personal_favourable_rate.setText(STFUConfig.sUser.getFavourable_rate() + "%");
            personal_signate.setText(STFUConfig.sUser.getSignature());
            Picasso.get()
                    .load(STFUConfig.sUser.getAvatar_url())
                    .resize(200, 200)
                    .centerCrop()
                    .into(personal_avator);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_mission_received:
                Intent intent = new Intent(getActivity(), ErrandReceivedActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_mission_released:
                Toast.makeText(getActivity(), "You clicked the misson_released", Toast.LENGTH_SHORT).show();
                break;
            case R.id.personal_edit:

                Intent intent1 = new Intent(getActivity(), EditActivity.class);
                intent1.putExtra("user", STFUConfig.sUser);

                //之后要实现获取返回值
                startActivity(intent1);

                break;
        }
    }
}
