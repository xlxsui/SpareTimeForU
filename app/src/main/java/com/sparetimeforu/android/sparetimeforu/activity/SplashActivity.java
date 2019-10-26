package com.sparetimeforu.android.sparetimeforu.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.sparetimeforu.android.sparetimeforu.fragment.SplashFragment;

/**
 * 启动Activity，加载启动时的画面
 * Created by Jin on 2019/10/22.
 * Email:17wjli6@stu.edu.cn
 */
public class SplashActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SplashFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // 没有虚拟按键
        if (!checkDeviceHasNavigationBar(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * 隐藏虚拟按键，状态栏
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (checkDeviceHasNavigationBar(this)) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }

        }
    }

    /**
     * 检测有没有虚拟按键
     * @param context 上下文
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {

        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey & !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }


}
