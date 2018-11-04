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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private NavigationView mNavigationView;



    public static STFUFragment newInstance() {
        return new STFUFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_stfu, container, false);


        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        TextView title = (TextView) view.findViewById(R.id.title_top);
        title.setText("汕大顺手邦");
        ImageView slideIcon = (ImageView) view.findViewById(R.id.menu_slide_icon);
        slideIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


        mMainFragmentLayout = (FrameLayout) view.findViewById(R.id.main_fragment);
        initAddLayout(R.layout.fragment_errand);


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
         * setup slider_menu navigation
         */




        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadErrandFragment();
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





























