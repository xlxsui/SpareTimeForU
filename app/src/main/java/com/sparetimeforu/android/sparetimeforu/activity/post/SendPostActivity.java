package com.sparetimeforu.android.sparetimeforu.activity.post;

import android.support.v4.app.Fragment;

import com.sparetimeforu.android.sparetimeforu.activity.SingleSwipeBackFragmentActivity;
import com.sparetimeforu.android.sparetimeforu.fragment.post.SendPostFragment;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/5.
 * Email:17wjli6@stu.edu.cn
 */
public class SendPostActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SendPostFragment.newInstance();
    }
}
