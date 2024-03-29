package com.sparetimeforu.android.sparetimeforu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.sparetimeforu.android.sparetimeforu.fragment.PictureFragment;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class PictureActivity extends SingleSwipeBackFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        Intent intent = this.getIntent();
        String photoUrl = intent.getStringExtra("photo_url");

        Bundle bundle = new Bundle();
        if (photoUrl != null) {
            bundle.putString("photo_url", photoUrl);
        }


        return PictureFragment.newInstance(bundle);
    }
}
