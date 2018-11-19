package com.sparetimeforu.android.sparetimeforu.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.sparetimeforu.android.sparetimeforu.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * HttpTest3_Json
 * Created by Jin on 2018/11/17.
 * Email:17wjli6@stu.edu.cn
 */

public abstract class SingleSwipeBackFragmentActivity extends SwipeBackActivity {
    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();

        }
    }
}
