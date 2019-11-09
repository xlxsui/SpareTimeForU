package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sparetimeforu.android.sparetimeforu.R;

/**
 * Created by HQY on 2018/12/2.
 */

public class Errand_received_fragment extends Fragment {

    public static int RECEIVED_MISSION_DOING = 0;//获取进行中任务的常量
    public static int RECEIVED_MISSION_DONE = 1;//获取进行中任务的常量

    private View view;
    private Toolbar toolbar;
    private BottomNavigationView mission_received_switch;
    private LinearLayout mLinearLayout;
    private FragmentManager mFragmentManager;
    private ErrandContentFragment mErrandFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mission_received, container, false);

        mFragmentManager = getFragmentManager();


        //一开始就显示进行中的任务

        loadMissionDoing();

        //设置toolbar
        toolbar = view.findViewById(R.id.personal_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView backIV = view.findViewById(R.id.menu_back_icon);
        backIV.setOnClickListener((v) -> getActivity().finish());

        //设置导航切换界面
        mission_received_switch = (BottomNavigationView) view.findViewById(R.id.mission_received_select);
        mission_received_switch.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mission_received_doing:
                        loadMissionDoing();
                        return true;

                    case R.id.mission_received_done:
                        loadMissionDone();
                        return true;
                }
                return false;
            }
        });


        return view;
    }


    private void loadMissionDoing() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mErrandFragment = (ErrandContentFragment) mFragmentManager.findFragmentById(R.id.mission_received_content);

        if (mErrandFragment == null) {

            mErrandFragment = new ErrandContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("mode", RECEIVED_MISSION_DOING);
            mErrandFragment.setArguments(bundle);

        } else {
            Bundle bundle = mErrandFragment.getArguments();
            if (bundle.getInt("mode") != RECEIVED_MISSION_DOING) {
                mErrandFragment = new ErrandContentFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("mode", RECEIVED_MISSION_DOING);
                mErrandFragment.setArguments(bundle1);
            }
        }

        mFragmentManager.beginTransaction().replace(R.id.mission_received_content, mErrandFragment).commit();

    }

    private void loadMissionDone() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mErrandFragment = (ErrandContentFragment) mFragmentManager.findFragmentById(R.id.mission_received_content);

        if (mErrandFragment == null) {

            mErrandFragment = new ErrandContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("mode", RECEIVED_MISSION_DONE);
            mErrandFragment.setArguments(bundle);

        } else {
            Bundle bundle = mErrandFragment.getArguments();
            if (bundle.getInt("mode") != RECEIVED_MISSION_DONE) {
                mErrandFragment = new ErrandContentFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("mode", RECEIVED_MISSION_DONE);
                mErrandFragment.setArguments(bundle1);

            }

        }

        mFragmentManager.beginTransaction().replace(R.id.mission_received_content, mErrandFragment).commit();
    }

}
