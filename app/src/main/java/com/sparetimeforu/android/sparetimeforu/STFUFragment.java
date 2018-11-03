package com.sparetimeforu.android.sparetimeforu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

    private TextView mTitle;
    private ImageView mSlideIcon;
    private Fragment mMenuFragment;
    private FrameLayout mMainFragmentLayout;

    private boolean isFragmentLoaded;

    public static STFUFragment newInstance() {
        return new STFUFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_stfu, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mMainFragmentLayout = (FrameLayout) view.findViewById(R.id.main_fragment);

        initAddLayout(R.layout.fragment_errand);


        mTitle = (TextView) view.findViewById(R.id.title_top);
        mTitle.setText("汕大顺手邦");

        /**
         * mSlideIcon
         */
        mSlideIcon = (ImageView) view.findViewById(R.id.menu_slide_icon);
        mSlideIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentLoaded) {
                    loadMenuFragment();
                    mTitle.setText("个人信息");
                } else {
                    if (mMenuFragment != null) {
                        if (mMenuFragment.isAdded()) {
                            hideMenuFragment();
                        }
                    }
                }
            }
        });

        /**
         * mSearchIcon
         */
        ImageView searchIcon = (ImageView) view.findViewById(R.id.menu_search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Spare time for you",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });


        return view;
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


    /**
     * load the slider
     */
    public void loadMenuFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mMenuFragment = fm.findFragmentById(R.id.slider_container);//find the container
        mSlideIcon.setImageResource(R.drawable.ic_up_arrow);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuFragment();//make the fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
            fragmentTransaction.add(R.id.slider_container, mMenuFragment);
            fragmentTransaction.commit();
        }
        isFragmentLoaded = true;
    }

    /**
     * hide the slider
     */
    public void hideMenuFragment() {
        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
        fragmentTransaction.remove(mMenuFragment);
        fragmentTransaction.commit();
        mSlideIcon.setImageResource(R.drawable.ic_menu_slide);
        isFragmentLoaded = false;
        mTitle.setText("汕大顺手邦");

    }

    public void loadErrandFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment errandFragment = fm.findFragmentById(R.id.errand_container);
        if (errandFragment == null) {
            errandFragment = new ErrandFragment();
        }

    }

}





























