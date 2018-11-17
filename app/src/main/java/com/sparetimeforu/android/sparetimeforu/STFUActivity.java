package com.sparetimeforu.android.sparetimeforu;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


public class STFUActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        Logger.addLogAdapter(new AndroidLogAdapter());
        return STFUFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        //处理当抽屉导航打开时按返回
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        DrawerLayout drawerLayout = (DrawerLayout) fragment.getView().findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }
}
