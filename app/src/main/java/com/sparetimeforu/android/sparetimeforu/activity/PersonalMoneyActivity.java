package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.fragment.FriendFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.PersonalMoneyFragment;

/**
 * Created by HQY on 2019/11/6.
 */

public class PersonalMoneyActivity extends SingleSwipeBackFragmentActivity {
    protected Fragment createFragment() {
        return PersonalMoneyFragment.newInstance();
    }
}