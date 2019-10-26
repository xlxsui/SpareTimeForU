package com.sparetimeforu.android.sparetimeforu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.fragment.Errand_received_fragment;
import com.sparetimeforu.android.sparetimeforu.fragment.PostReleasedFragment;

/**
 * Created by HQY on 2019/10/7.
 */


public class PostReleasedActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PostReleasedFragment();
    }
}