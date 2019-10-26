package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.fragment.ConversationListFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.SystemMessageListFragment;

/**
 * Created by HQY on 2019/10/2.
 */

public class SystemMessageListActivity extends SingleSwipeBackFragmentActivity {//用来显示私信
    @Override
    protected Fragment createFragment() {
        return SystemMessageListFragment.newInstance();
    }
}