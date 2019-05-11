package com.sparetimeforu.android.sparetimeforu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.fragment.EditFragment;

/**
 * Created by HQY on 2018/11/25.
 */

public class EditActivity extends SingleSwipeBackFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new EditFragment();
    }

}
