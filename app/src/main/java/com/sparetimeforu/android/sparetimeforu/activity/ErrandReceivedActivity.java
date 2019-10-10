package com.sparetimeforu.android.sparetimeforu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.fragment.EditFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.ErrandContentFragment;
import com.sparetimeforu.android.sparetimeforu.fragment.Errand_received_fragment;

/**
 * Created by HQY on 2018/12/2.
 */

public class ErrandReceivedActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new Errand_received_fragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
