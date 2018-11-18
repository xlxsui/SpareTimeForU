package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.activity.SignUpActivity;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 * Email:17wjli6@stu.edu.cn
 */

public class LoginFragment extends Fragment {
    private ImageView m22;
    private ImageView m33;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignUpButton;
    private Button mLoginButton;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        m22 = (ImageView) view.findViewById(R.id.ic_icon_left);
        m33 = (ImageView) view.findViewById(R.id.ic_icon_right);
        mEmail = (EditText) view.findViewById(R.id.et_email);
        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    m22.setImageDrawable(getResources().getDrawable(R.drawable.ic_22));
                    m33.setImageDrawable(getResources().getDrawable(R.drawable.ic_33));
                }
            }
        });
        mPassword = (EditText) view.findViewById(R.id.et_password_login);
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    m22.setImageDrawable(getResources().getDrawable(R.drawable.ic_22_hide));
                    m33.setImageDrawable(getResources().getDrawable(R.drawable.ic_33_hide));

                }
            }
        });

        mSignUpButton = (Button) view.findViewById(R.id.btn_sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        return view;
    }
}
