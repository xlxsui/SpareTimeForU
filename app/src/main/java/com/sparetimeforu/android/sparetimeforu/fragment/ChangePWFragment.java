package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFU;
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
 * Created by Jin on 2019/5/27.
 * Email:17wjli6@stu.edu.cn
 */
public class ChangePWFragment extends Fragment {
    static String url;


    @BindView(R.id.change_pw_email)
    EditText mEmailEditText;
    @BindView(R.id.change_pw_password)
    EditText mP1EditText;
    @BindView(R.id.change_pw_password2)
    EditText mP2EditText;
    @BindView(R.id.change_pw_verification_code)
    EditText mVCEditText;
    @BindView(R.id.change_pw_btn_send_verification_code)
    Button mSendVCButton;

    private CountDownTime mTime;
    STFU app;

    public static Fragment newInstance() {
        return new ChangePWFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        app = (STFU) getActivity().getApplication();
        url = app.getHOST() + "/user/change_password";

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);

        mTime = new CountDownTime(60000, 1000);//倒计时的
        return view;
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.change_pw_btn_send_verification_code)
    public void sendVC() {
        if (checkInformation() != 1) {
            sendVerificationCode();
            mTime.start(); //开始计时
        } else {
            Toast.makeText(getActivity(), "邮箱不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.change_pw_confirm)
    public void confirm() {
        switch (checkInformation()) {
            case 0:
                sendChangePWRequest();
                break;
            case 1:
                Toast.makeText(getActivity(), "邮箱不能为空!", Toast.LENGTH_SHORT).show();
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

    public void sendVerificationCode() {
        String emailPrefix = mEmailEditText.getText().toString();
        String email = emailPrefix + "@stu.edu.cn";//完整的邮箱
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

    public void sendChangePWRequest() {
        //发送注册post请求
        String emailPrefix = mEmailEditText.getText().toString();
        String email = emailPrefix + "@stu.edu.cn";//完整的邮箱
        String password1 = mP1EditText.getText().toString();
        String password2 = mP2EditText.getText().toString();
        String verificationCode = mVCEditText.getText().toString();

        FormBody body = new FormBody.Builder()
                .add("email", email)
                .add("password1", password1)
                .add("password2", password2)
                .add("verification_code", verificationCode)
                .add("request_type", "change_password")
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
                                    Toast.makeText(getActivity(), "修改密码成功", Toast.LENGTH_SHORT).show();
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
     * @return 0 成功，1 邮箱为空，3 密码不匹配，4 密码少于8位，5 验证码为空
     */
    private int checkInformation() {
        if (Objects.equals(mEmailEditText.getText().toString(), "")) {
            return 1;
        }
        if (!Objects.equals(mP1EditText.getText().toString(), mP2EditText.getText().toString())) {
            return 3;
        }
        if (mP1EditText.getText().toString().length() < 8) {
            return 4;
        }
        if (Objects.equals(mVCEditText.getText().toString(), "")) {
            return 5;
        }
        return 0;
    }

    /**
     * 使用android封装好的 CountDownTimer
     * 创建一个类继承 CountDownTimer
     */
    class CountDownTime extends CountDownTimer {

        //构造函数  第一个参数代表总的计时时长  第二个参数代表计时间隔  单位都是毫秒
        public CountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {  //每计时一次回调一次该方法
            mSendVCButton.setClickable(false);
            mSendVCButton.setText(l / 1000 + "秒后重新发送");
        }

        @Override
        public void onFinish() { //计时结束回调该方法
            mSendVCButton.setClickable(true);
            mSendVCButton.setText("重新发送验证码");
        }
    }
}
