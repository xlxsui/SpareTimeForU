package com.sparetimeforu.android.sparetimeforu;

import android.content.Context;
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

/**
 * Created by Jin on 2018/11/2.
 */

public class STFUFragment extends Fragment {
    private Toolbar mToolbar;
    private FrameLayout mMainFragmentLayout;
    private Fragment mErrandFragment;
    private BottomNavigationView mBottomNavigationView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerNavigationView;

    private static final String TAG = "STFUFragment";


    public static STFUFragment newInstance() {
        return new STFUFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_stfu, container, false);


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
         * main fragment and its layout
         */
        mMainFragmentLayout = (FrameLayout) view.findViewById(R.id.main_fragment);
        initAddLayout(R.layout.fragment_errand);
        loadErrandFragment();


        /**
         * setup bottom navigation
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
                                return true;
                            case R.id.navigation_second_hand:
                                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT)
                                        .show();
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
         * setup slider_menu navigation drawer
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


        mErrandFragment.getView().setFocusableInTouchMode(true);
        mErrandFragment.getView().requestFocus();
        mErrandFragment.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                /**
                 * deal with back press
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
     * add layout to main_fragment
     *
     * @param layout
     */
    protected void initAddLayout(int layout) {
        LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity) getActivity())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null);
        mMainFragmentLayout.addView(view);
    }


    public void loadErrandFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mErrandFragment = fm.findFragmentById(R.id.errand_container);
        mErrandFragment = new ErrandFragment();
        fm.beginTransaction()
                .add(R.id.errand_container, mErrandFragment)
                .commit();
        Log.d("Errand", "-----It is time to load the Errand Fragment");

    }

}





























