package com.sparetimeforu.android.sparetimeforu.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.util.QueryPreferences;
import com.sparetimeforu.android.sparetimeforu.util.StatusBarUtils;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/26.
 * Email:17wjli6@stu.edu.cn
 */


public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setColor(this, getColor(R.color.colorPrimaryDark));
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("赏金");
        sliderPage.setDescription("任何人都可以为你解决小需求");
        sliderPage.setImageDrawable(R.drawable.ic_22);
        sliderPage.setBgColor(getColor(R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        sliderPage.setTitle("闲置");
        sliderPage.setDescription("来体验一下汕大版闲鱼");
        sliderPage.setImageDrawable(R.drawable.ic_22_hide);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        sliderPage.setTitle("问答");
        sliderPage.setDescription("有疑惑，问问大家看看");
        sliderPage.setImageDrawable(R.drawable.ic_33);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        sliderPage.setTitle("寻物");
        sliderPage.setDescription("啥，东西丢了，大家帮忙找找，报酬你说");
        sliderPage.setImageDrawable(R.drawable.ic_33_hide);
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("权限申请");
        sliderPage.setDescription("我们需要这些权限才能正常工作");
        sliderPage.setImageDrawable(R.drawable.ic_22);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        askForPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 5);


        setFadeAnimation();
        showSkipButton(false);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        QueryPreferences.setIsFirstStart(this);
        Intent intent = new Intent(this, STFUActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        QueryPreferences.setIsFirstStart(this);
        Intent intent = new Intent(this, STFUActivity.class);
        startActivity(intent);
        this.finish();
    }
}
