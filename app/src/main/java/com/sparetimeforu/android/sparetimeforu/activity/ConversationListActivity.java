package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.activity.SingleSwipeBackFragmentActivity;
import com.sparetimeforu.android.sparetimeforu.fragment.ConversationListFragment;

/**
 * Created by HQY on 2019/7/20.
 */

public class ConversationListActivity extends SingleSwipeBackFragmentActivity {//用来显示私信
    @Override
    protected Fragment createFragment() {
        return ConversationListFragment.newInstance();
    }
}
