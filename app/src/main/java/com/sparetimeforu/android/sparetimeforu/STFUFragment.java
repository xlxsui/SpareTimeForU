package com.sparetimeforu.android.sparetimeforu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Jin on 2018/11/2.
 */

public class STFUFragment extends Fragment {
    private Toolbar mToolbar;
    private FrameLayout mMainFragmentLayout;
    private Fragment mErrandFragment;
    private Fragment mIdleThingFragment;
    private Fragment mStudyFragment;
    private Fragment mSearchThingFragment;
    private BottomNavigationView mBottomNavigationView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerNavigationView;
    FragmentManager mFm;


    private static final String TAG = "STFUFragment";


    public static STFUFragment newInstance() {
        return new STFUFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_stfu, container, false);
        mFm = getActivity().getSupportFragmentManager();


        /**
         * toolbar and its component
         */
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        TextView title = (TextView) view.findViewById(R.id.title_top);
        title.setText("汕大顺手邦");
        ImageView slideIcon = (ImageView) view.findViewById(R.id.menu_slide_icon);
        slideIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);

            }
        });
        ImageView searchIcon = (ImageView) view.findViewById(R.id.menu_search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Spare time for you!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        /**
         * 主片段及其布局
         */
        mMainFragmentLayout = (FrameLayout) view.findViewById(R.id.main_fragment);
        loadErrandFragment();


        /**
         * 设置底部导航
         */
        mBottomNavigationView =
                (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.navigation_errand:
                                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                        .show();
                                loadErrandFragment();
                                return true;
                            case R.id.navigation_second_hand:
                                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                        .show();
                                loadIdleThingFragment();
                                return true;
                            case R.id.navigation_study:
                                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                        .show();
                                return true;
                            case R.id.navigation_search:
                                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                        .show();
                                return true;
                        }
                        return false;

                    }
                });


        /**
         * 设置抽屉导航
         */
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mDrawerNavigationView = (NavigationView) view.findViewById(R.id.slider_menu);
        View mDrawerHeaderView = mDrawerNavigationView.getHeaderView(0);

        final ImageView mAvatar = (ImageView) mDrawerHeaderView.findViewById(R.id.slider_menu_avatar);
        try {
            Random random = new Random();
            int i = random.nextInt(11) + 1;
            InputStream ims = getActivity().getAssets().open("avatar/ic_avatar" + i + ".jpg");
            Drawable avatar = Drawable.createFromStream(ims, null);
            mAvatar.setImageDrawable(avatar);
        } catch (IOException e) {
            Log.e(TAG, "onCreateView: ", e);
        }
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Avatar", Toast.LENGTH_SHORT)
                        .show();
            }
        });


        mDrawerNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.slider_menu_task:
                        Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.slider_menu_personal_letter:
                        Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.slider_menu_login:
                        Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.slider_menu_exit:
                        getActivity().finish();
                        getActivity().moveTaskToBack(true);
                        System.exit(0);
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Fragment currentFragment = null;
        if (mErrandFragment != null) {
            currentFragment = mErrandFragment;
        } else if (mIdleThingFragment != null) {
            currentFragment = mIdleThingFragment;
        }

        currentFragment.getView().setFocusableInTouchMode(true);
        currentFragment.getView().requestFocus();
        currentFragment.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /**
                 * deal with back press in drawer navigation
                 */
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.d(TAG, "-----Back pressed");
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }



    /**
     * 加载Item Fragment到main_fragment布局
     */
    public void loadErrandFragment() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        View view = inflater.inflate(R.layout.fragment_errand, mMainFragmentLayout);
        mErrandFragment = mFm.findFragmentById(R.id.errand_container);

        if (mErrandFragment == null) {
            //假如fragment为空，这时才重新创建一个，并且提交显示
            mErrandFragment = new ErrandFragment();
        }
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mErrandFragment)
                .commit();
    }

    public void loadIdleThingFragment() {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //把layout生成一个view对象
        View view = inflater.inflate(R.layout.fragment_idle_thing, mMainFragmentLayout);
        mIdleThingFragment = mFm.findFragmentById(R.id.idle_thing_container);
        if (mIdleThingFragment == null) {
            mIdleThingFragment = new IdleThingFragment();
        }
        mFm.beginTransaction()
                .replace(R.id.main_fragment, mIdleThingFragment)
                .commit();
    }

    public void loadStudyFragment() {

    }

    public void loadSearchThingFragment() {

    }

}





























