package com.sparetimeforu.android.sparetimeforu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.activity.EditActivity;
import com.sparetimeforu.android.sparetimeforu.activity.ErrandReceivedActivity;
import com.sparetimeforu.android.sparetimeforu.activity.LoginActivity;
import com.sparetimeforu.android.sparetimeforu.activity.PictureActivity;
import com.sparetimeforu.android.sparetimeforu.activity.PostReleasedActivity;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.sparetimeforu.android.sparetimeforu.util.StatusBarUtils;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sparetimeforu.android.sparetimeforu.fragment.post.ErrandPostFragment.PHOTO_URL;

/**
 * Created by HQY on 2018/11/15.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener {

    private final int LOGIN_CODE = 1;
    //用户个人信息
    private TextView personal_favourable_rate, personal_nickname, personal_signate;
    private ImageView personal_avator;

    //用户界面需要相应的控件
    private TextView personal_mission_received, personal_mission_released;

    private Toolbar toolbar;
    private ImageView personal_go_back;
    private TextView personal_edit;//点击编辑按钮
    private ImageView mBGImageView;

    @BindView(R.id.data_layout)
    RelativeLayout mBGLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, container, false);
        ButterKnife.bind(this, view);

        //初始化控件
        personal_avator = (ImageView) view.findViewById(R.id.personal_avator);
        personal_nickname = (TextView) view.findViewById(R.id.personal_nickname);
        personal_signate = (TextView) view.findViewById(R.id.personal_signate);
        personal_mission_received = (TextView) view.findViewById(R.id.personal_mission_received);
        personal_mission_released = (TextView) view.findViewById(R.id.personal_mission_released);
        personal_favourable_rate = (TextView) view.findViewById(R.id.personal_favourable_rate);
        personal_edit = (TextView) view.findViewById(R.id.personal_edit);
        mBGImageView = new ImageView(getContext());


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LOGIN_CODE:
                    STFUConfig.sUser = (User) data.getSerializableExtra("user");
                    updateViews();
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViews();
    }

    @OnClick(R.id.personal_avator)
    public void clickAvatar() {

        if (STFUConfig.sUser != null) {
            Intent intent = new Intent(getActivity(), PictureActivity.class);
            intent.putExtra(PHOTO_URL, STFUConfig.HOST + "/static/avatar/" + STFUConfig.sUser.getAvatar_url());
            getActivity().startActivity(intent);
        } else {
            Intent intent1 = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent1, LOGIN_CODE);
        }

    }

    @OnClick(R.id.data_layout)
    public void clickBG() {

        if (STFUConfig.sUser != null) {
            Intent intent = new Intent(getActivity(), PictureActivity.class);
            intent.putExtra(PHOTO_URL, STFUConfig.HOST + "/static/personal_background/" + STFUConfig.sUser.getBg_url());
            getActivity().startActivity(intent);
        } else {
            Intent intent1 = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent1, LOGIN_CODE);
        }

    }


    private void updateViews() {
        if (STFUConfig.sUser != null) {
            personal_nickname.setText(STFUConfig.sUser.getNickname());
            personal_favourable_rate.setText(STFUConfig.sUser.getFavourable_rate() + "%");
            personal_signate.setText(STFUConfig.sUser.getSignature());
            //设置头像
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/avatar/" + STFUConfig.sUser.getAvatar_url())
                    .resize(200, 200)
                    .centerCrop()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(personal_avator);
            //设置背景图片
            Picasso.get()
                    .load(STFUConfig.HOST + "/static/personal_background/" + STFUConfig.sUser.getBg_url())
                    .resize(1920, 1080)
                    .centerCrop()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(mBGImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mBGImageView.getDrawable().setColorFilter(0x33000000, PorterDuff.Mode.SRC_ATOP);//小滤镜
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
            case R.id.personal_mission_received:
                Intent intent = new Intent(getActivity(), ErrandReceivedActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_mission_released:
                Intent intent1 = new Intent(getActivity(), PostReleasedActivity.class);
                startActivity(intent1);
                break;
            case R.id.personal_edit:
                if (VerifyUtil.isLogin(getActivity())) {
                    Intent intent2 = new Intent(getActivity(), EditActivity.class);
                    intent2.putExtra("user", STFUConfig.sUser);
                    //之后要实现获取返回值
                    startActivity(intent2);
                }
                break;
        }
    }
}
