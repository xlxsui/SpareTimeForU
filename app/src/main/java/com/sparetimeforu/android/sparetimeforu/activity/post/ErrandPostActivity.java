package com.sparetimeforu.android.sparetimeforu.activity.post;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.activity.SingleSwipeBackFragmentActivity;
import com.sparetimeforu.android.sparetimeforu.fragment.post.ErrandPostFragment;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class ErrandPostActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return ErrandPostFragment.newInstance();
    }
}
