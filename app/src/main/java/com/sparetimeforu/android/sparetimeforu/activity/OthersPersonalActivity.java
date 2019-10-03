package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.fragment.OthersPersonalFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.PersonalFragment;

/**
 * Created by HQY on 2019/10/3.
 */


public class OthersPersonalActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new OthersPersonalFragment();
    }
}