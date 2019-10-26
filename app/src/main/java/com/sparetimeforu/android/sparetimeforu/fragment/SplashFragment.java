package com.sparetimeforu.android.sparetimeforu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.IntroActivity;
import com.sparetimeforu.android.sparetimeforu.activity.STFUActivity;
import com.sparetimeforu.android.sparetimeforu.activity.SplashActivity;
import com.sparetimeforu.android.sparetimeforu.util.QueryPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/22.
 * Email:17wjli6@stu.edu.cn
 */
public class SplashFragment extends Fragment {

    @BindView(R.id.splash_animation_view)
    LottieAnimationView mLottieAnimationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);

        mLottieAnimationView.setImageAssetsFolder("images/");
        mLottieAnimationView.setAnimation("launcher.json");
        mLottieAnimationView.playAnimation();


        new CountDownTime(3000, 1000).start();
        return view;
    }

    class CountDownTime extends CountDownTimer {
        /**
         * 构造函数  第一个参数代表总的计时时长  第二个参数代表计时间隔  单位都是毫秒
         *
         * @param millisInFuture    总的计时时长
         * @param countDownInterval 计时间隔
         */
        public CountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {

            //是否显示介绍界面
            if (QueryPreferences.getIsFirstStart(getActivity())) {
                Intent intent = new Intent(getActivity(), IntroActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), STFUActivity.class);
                startActivity(intent);
            }

            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }
}
