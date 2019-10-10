package com.sparetimeforu.android.sparetimeforu.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/5.
 * Email:17wjli6@stu.edu.cn
 */

public class StatusBarUtils {
    /**
     * 获取状态栏高度的方法
     *
     * @param context 上下文
     * @return 高度
     */
    public static int getHeight(Context context) {
        int statusBarHeight = 0;
        try {
            int resourceId = context.getResources().getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return statusBarHeight;
    }

    /**
     * 给window的状态栏设置颜色
     *
     * @param window 窗口
     * @param color  颜色
     */
    public static void setColor(@NonNull Window window, @ColorInt int color) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(color);
        setTextDark(window, !isDarkColor(color));
    }

    public static void setColor(Context context, @ColorInt int color) {
        if (context instanceof Activity) {
            setColor(((Activity) context).getWindow(), color);
            setTextDark(((Activity) context).getWindow(), !isDarkColor(color));
        }

    }

    /**
     * 设置状态栏文字和图标的深色模式和浅色模式
     *
     * @param window 窗口
     * @param isDark 是否要深改色
     */
    private static void setTextDark(Window window, boolean isDark) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            if (isDark) {
                decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setTextDark(Context context, boolean isDark) {
        if (context instanceof Activity) {
            setTextDark(((Activity) context).getWindow(), isDark);
        }
    }

    /**
     * 根据状态栏背景颜色的深浅而自动设置文字的颜色，判断颜色深浅
     *
     * @param color 状态栏背景颜色
     * @return 是否深色
     */
    public static boolean isDarkColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

}

