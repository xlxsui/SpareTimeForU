package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.fragment.PersonalFragment;

/**
 * Created by HQY on 2018/11/15.
 */

public class PersonalActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PersonalFragment();
    }
}
