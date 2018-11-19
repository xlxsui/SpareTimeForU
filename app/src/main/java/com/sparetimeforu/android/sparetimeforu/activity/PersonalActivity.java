package com.sparetimeforu.android.sparetimeforu.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.fragment.PersonalFragment;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by HQY on 2018/11/15.
 */

public class PersonalActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_container);
        FragmentManager fm=(FragmentManager) getSupportFragmentManager();

        Fragment fragment=fm.findFragmentById(R.id.Personal_fragment);

        if(fragment==null){
            fragment=new PersonalFragment();
            fm.beginTransaction().add(R.id.Personal_fragment,fragment).commit();
        }

    }
}
