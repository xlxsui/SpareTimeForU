package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.sparetimeforu.android.sparetimeforu.fragment.FriendFragment;

/**
 * Created by HQY on 2019/7/20.
 */

public class FriendActivity extends SingleSwipeBackFragmentActivity {
    protected Fragment createFragment() {
        return FriendFragment.newInstance();
    }
}
