package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.STFUActivity;
import com.sparetimeforu.android.sparetimeforu.activity.SignUpActivity;
import com.sparetimeforu.android.sparetimeforu.user.User;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 * Email:17wjli6@stu.edu.cn
 */

public class LoginFragment extends Fragment {
    private static String LoginServerUrl="https://www.easy-mock.com/mock/5bebc836e0c6d321cade7458/success/LoginServer";
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


        mLoginButton=(Button)view.findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new MyListener());


        return view;
    }



    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login:

                    Log.i("test1",LoginServerUrl + "#!email=" + mEmail.getText().toString() + "&password" + mPassword.getText().toString());
                    OkHttpUtil.sendLoginOkHttpRequest(LoginServerUrl+"?email="+ mEmail.getText().toString()+"&password=" + mPassword.getText().toString(),
                            new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginFragment.this.getActivity(),"无法获取用户信息，请检查网络是否正常",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final User user=HandleMessageUtil.handleLoginMessage(response.body().string());
                                    if(user==null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginFragment.this.getActivity(),"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent=new Intent(getActivity(), STFUActivity.class);
                                                intent.putExtra("user",user);
                                                startActivity(intent);
                                                Log.i("test1","获取数据成功");
                                            }
                                        });
                                    }
                                }
                            });
                    break;

            }
        }
    }
}
