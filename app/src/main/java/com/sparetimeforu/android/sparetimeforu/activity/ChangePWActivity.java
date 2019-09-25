package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.fragment.ChangePWFragment;

/**
 * SpareTimeForU
 * Created by Jin on 2019/5/27.
 * Email:17wjli6@stu.edu.cn
 */
public class ChangePWActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return ChangePWFragment.newInstance();
    }
}
