package com.sparetimeforu.android.sparetimeforu;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HQY on 2018/11/15.
 */

public class PersonalActivity extends AppCompatActivity {
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
