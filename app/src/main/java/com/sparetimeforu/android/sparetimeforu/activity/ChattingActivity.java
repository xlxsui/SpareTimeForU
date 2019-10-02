package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.sparetimeforu.android.sparetimeforu.fragment.ChattingFragment;

/**
 * Created by HQY on 2019/7/20.
 */

public class ChattingActivity extends SingleSwipeBackFragmentActivity {
    protected Fragment createFragment() {
        return ChattingFragment.newInstance();
    }
}
