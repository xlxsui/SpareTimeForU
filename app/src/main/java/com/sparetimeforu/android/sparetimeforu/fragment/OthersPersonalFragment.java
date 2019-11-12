package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.ChattingActivity;
import com.sparetimeforu.android.sparetimeforu.activity.EditActivity;
import com.sparetimeforu.android.sparetimeforu.activity.ErrandReceivedActivity;
import com.sparetimeforu.android.sparetimeforu.adapter.FriendAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.SystemMessageSendUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.service.JCommonService;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by HQY on 2019/10/1.
 */

public class OthersPersonalFragment extends Fragment implements View.OnClickListener  {


    //所查看用户个人信息
    private TextView others_add_friend, others_nickname, others_signate;
    private ImageView others_avator;

    //用户界面需要相应的控件
    private TextView others_private_chatting, others_mission_released;

    private Toolbar toolbar;
    private ImageView others_go_back;
    private TextView others_edit;//点击编辑按钮
    private ImageView mBGImageView;

    @BindView(R.id.data_layout)
    RelativeLayout mBGLayout;

    private User others_user=null;//存储他人界面上的用户信息

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        JMessageClient.init(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.others_message_fragment, container, false);
        ButterKnife.bind(this, view);

        //初始化控件
        others_avator = (ImageView) view.findViewById(R.id.others_avator);
        others_nickname = (TextView) view.findViewById(R.id.others_nickname);
        others_signate = (TextView) view.findViewById(R.id.others_signate);
        others_private_chatting = (TextView) view.findViewById(R.id.others_private_chatting);
        others_mission_released = (TextView) view.findViewById(R.id.others_mission_released);
        others_add_friend = (TextView) view.findViewById(R.id.others_add_friend);
        mBGImageView = new ImageView(getContext());


        //给TextView添加监听

        others_private_chatting.setOnClickListener(this);
        others_add_friend.setOnClickListener(this);


        //设置Toolbar
        toolbar = view.findViewById(R.id.personal_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        //设置Toolbar中返回按钮的点击事件

        others_go_back = (ImageView) view.findViewById(R.id.persnal_go_back);
        others_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        init_others_user();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void init_others_user(){//初始化他人界面上的用户信息
        String url=STFUConfig.HOST+"/user/get_user";
        Intent intent=getActivity().getIntent();
        //发送请求到服务器拿信息

        FormBody formBody=new FormBody.Builder().add("user_email",intent.getStringExtra("user_Email")).build();
        OkHttpUtil.sendOkHttpPostRequest(url, formBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"请求用户数据失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                others_user= HandleMessageUtil.handleLoginMessage(response.body().string());
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateViews();
                    }
                });

            }
        });
    }


    private void updateViews() {
        if (others_user != null) {
            others_nickname.setText(others_user.getNickname());
            others_signate.setText(others_user.getSignature());
            Logger.i(others_user.toString());
            //设置头像
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/avatar/" + others_user.getAvatar_url())
                    .resize(200, 200)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(others_avator);
            //设置背景图片
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/personal_background/" + others_user.getBg_url())
                    .resize(1920, 1080)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(mBGImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mBGImageView.getDrawable().setColorFilter(0x33000000, PorterDuff.Mode.SRC_ATOP);
                            mBGLayout.setBackground(mBGImageView.getDrawable());
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });

        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.others_add_friend:
                Snackbar.make(getView(),"点击了加好友按钮", BaseTransientBottomBar.LENGTH_SHORT).show();
                ContactManager.sendInvitationRequest(others_user.getEmail(), null, "交个朋友", new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if(i==0)
                        Toast.makeText(getContext(),"已发送好友请求",Toast.LENGTH_SHORT).show();
                        SystemMessageSendUtil.send_System_message_add_friend(others_user.getEmail());
                    }
                });
                break;
            case R.id.others_private_chatting:
                Intent intent1=new Intent(getActivity(), ChattingActivity.class);
                intent1.putExtra("username",others_user.getEmail());//将聊天对象的用户名传进去
                getActivity().startActivity(intent1);
                break;
            case R.id.others_mission_released:
                break;
        }
    }
}
