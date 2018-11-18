package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sparetimeforu.android.sparetimeforu.R;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class SignUpFragment extends Fragment {
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);


        return view;
    }
}
