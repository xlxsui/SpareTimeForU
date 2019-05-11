package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class SignUpFragment extends Fragment {

    static String url = STFUConfig.HOST + "/auth/signup";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_verification_code)
    EditText mVerificationCode;
    @BindView(R.id.btn_send_verification_code)
    Button mBtnVerificationCode;

    private CountDownTime mTime;

    EditText et_username;
    EditText et_username_back;
    EditText et_nickname;
    EditText et_password_reg;
    EditText et_password2_reg;
    Button btn_signup;

    /*
    * et_password2_reg
    et_username
    et_username_back
    et_nickname
    et_password_reg
    et_password2_reg
    btn_signup
    * */

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        et_nickname = (EditText) view.findViewById(R.id.et_nickname);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_username_back = (EditText) view.findViewById(R.id.et_username_back);
        et_password2_reg = (EditText) view.findViewById(R.id.et_password2_reg);
        et_password_reg = (EditText) view.findViewById(R.id.et_password_reg);
        btn_signup = (Button) view.findViewById(R.id.btn_signup);

        mTime = new CountDownTime(60000, 1000);//倒计时的
        return view;
    }


    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_send_verification_code)
    public void sendVCode() {
        if (checkInformation() != 1) {
            sendVerificationCode();
            mTime.start(); //开始计时
        } else {
            Toast.makeText(getActivity(), "邮箱不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_signup)
    public void signUp() {
        switch (checkInformation()) {
            case 0:
                sendSignUpPostRequest();
                break;
            case 1:
                Toast.makeText(getActivity(), "邮箱不能为空!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getActivity(), "昵称不能为空!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getActivity(), "密码不匹配!", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getActivity(), "密码不能少于8位!", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(getActivity(), "验证码不能为空!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * @return 0 成功，1 邮箱为空，2 昵称为空，3 密码不匹配，4 密码少于8位，5 验证码为空
     */
    private int checkInformation() {
        if (Objects.equals(et_username.getText().toString(), "")) {
            return 1;
        }
        if (Objects.equals(et_nickname.getText().toString(), "")) {
            return 2;
        }
        if (!Objects.equals(et_password_reg.getText().toString(), et_password2_reg.getText().toString())) {
            return 3;
        }
        if (et_password_reg.getText().toString().length() < 8) {
            return 4;
        }
        if (Objects.equals(mVerificationCode.getText().toString(), "")) {
            return 5;
        }
        return 0;
    }

    public void sendVerificationCode() {
        String username = et_username.getText().toString();
        String email = username + "@stu.edu.cn";//完整的邮箱
        FormBody body = new FormBody.Builder()
                .add("email", email)
                .add("request_type", "send_verification_code")
                .build();

        OkHttpUtil.sendOkHttpPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response_body = response.body().string();
                Logger.i(response_body);
                try {
                    JSONObject jsonObject = new JSONObject(response_body);
                    String status = jsonObject.getString("status");
                    if (Objects.equals(status, "success")) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "发送验证码成功，请到邮箱查收！", Toast.LENGTH_SHORT).show();
                        });
                    } else if (Objects.equals(status, "error")) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getActivity(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Logger.e(e.toString());
                            }
                        });
                    }
                } catch (JSONException e) {
                    Logger.e(e.toString());
                }
            }
        });
    }


    public void sendSignUpPostRequest() {
        //发送注册post请求
        String nickname = et_nickname.getText().toString();
        String username = et_username.getText().toString();
        String email = username + "@stu.edu.cn";//完整的邮箱
        String password1 = et_password_reg.getText().toString();
        String password2 = et_password2_reg.getText().toString();
        String verificationCode = mVerificationCode.getText().toString();

        FormBody body = new FormBody.Builder()
                .add("email", email)
                .add("nickname", nickname)
                .add("password1", password1)
                .add("password2", password2)
                .add("verification_code", verificationCode)
                .add("request_type", "sign_up")
                .build();

        //发送请求
        OkHttpUtil.sendOkHttpPostRequest(url, body,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "网络请求错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String response_body = response.body().string();
                        Logger.i(response_body);
                        try {
                            JSONObject jsonObject = new JSONObject(response_body);
                            String status = jsonObject.getString("status");
                            if (Objects.equals(status, "success")) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                });
                            } else if (Objects.equals(status, "error")) {
                                getActivity().runOnUiThread(() -> {
                                    try {
                                        Toast.makeText(getActivity(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        Logger.e(e.toString());
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Logger.e(e.toString());
                        }
                    }
                });
    }


    /**
     * 第一种方法 使用android封装好的 CountDownTimer
     * 创建一个类继承 CountDownTimer
     */
    class CountDownTime extends CountDownTimer {

        //构造函数  第一个参数代表总的计时时长  第二个参数代表计时间隔  单位都是毫秒
        public CountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {  //每计时一次回调一次该方法
            mBtnVerificationCode.setClickable(false);
            mBtnVerificationCode.setText(l / 1000 + "秒后重新发送");
        }

        @Override
        public void onFinish() { //计时结束回调该方法
            mBtnVerificationCode.setClickable(true);
            mBtnVerificationCode.setText("重新发送验证码");
        }
    }

}
