package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.fragment.SignUpFragment;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class SignUpActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SignUpFragment.newInstance();
    }
}
