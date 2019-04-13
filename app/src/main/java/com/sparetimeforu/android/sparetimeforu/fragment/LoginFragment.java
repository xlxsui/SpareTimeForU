package com.sparetimeforu.android.sparetimeforu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.STFUActivity;
import com.sparetimeforu.android.sparetimeforu.activity.SignUpActivity;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 * Email:17wjli6@stu.edu.cn
 */

public class LoginFragment extends Fragment {
    private static String LoginServerUrl = "http://172.16.85.249:5000/auth/login";
    private ImageView m22;
    private ImageView m33;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignUpButton;
    private Button mLoginButton;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        m22 = (ImageView) view.findViewById(R.id.ic_icon_left);
        m33 = (ImageView) view.findViewById(R.id.ic_icon_right);
        mEmail = (EditText) view.findViewById(R.id.et_email);
        mEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                m22.setImageDrawable(getResources().getDrawable(R.drawable.ic_22));
                m33.setImageDrawable(getResources().getDrawable(R.drawable.ic_33));
            }
        });
        mPassword = (EditText) view.findViewById(R.id.et_password_login);
        mPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                m22.setImageDrawable(getResources().getDrawable(R.drawable.ic_22_hide));
                m33.setImageDrawable(getResources().getDrawable(R.drawable.ic_33_hide));

            }
        });

        mSignUpButton = (Button) view.findViewById(R.id.btn_sign_up);
        mSignUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivityForResult(intent, 0);
        });


        mLoginButton = (Button) view.findViewById(R.id.btn_login);


        return view;
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (!Objects.equals(mEmail.getText().toString(), "") &&
                !Objects.equals(mPassword.getText().toString(), "")) {
            FormBody body = new FormBody.Builder().
                    add("email", mEmail.getText().toString() + "@stu.edu.cn").
                    add("password", mPassword.getText().toString()).
                    add("request_type", "login").
                    build();

            OkHttpUtil.sendOkHttpPostRequest(LoginServerUrl, body,
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(() -> Toast.makeText(LoginFragment.this.getActivity(),
                                    "无法获取用户信息，请检查网络是否正常", Toast.LENGTH_SHORT).show());

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final User user = HandleMessageUtil.handleLoginMessage(response.body().string());
                            if (user == null) {
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(LoginFragment.this.getActivity(),
                                                "用户名或者密码错误", Toast.LENGTH_SHORT).show());
                            } else {
                                getActivity().runOnUiThread(() -> {
                                    Intent intent = new Intent(getActivity(), STFUActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    Logger.i("获取数据成功");
                                });
                            }
                        }
                    });
        } else {
            Snackbar.make(getView(), "请输入邮箱和密码！", Snackbar.LENGTH_SHORT).show();
        }

    }
}
