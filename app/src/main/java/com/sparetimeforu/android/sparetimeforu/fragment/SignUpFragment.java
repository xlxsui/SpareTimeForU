package com.sparetimeforu.android.sparetimeforu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/18.
 * Email:17wjli6@stu.edu.cn
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {

    static String url = "http://172.16.92.161:5000/auth/signup";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
        btn_signup.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_signup) {

            sendPostRequest();

        }
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    private void sendPostRequest(){
        //发送注册post请求
        Toast.makeText(getActivity(), "Send Post!", Toast.LENGTH_SHORT).show();
        String nickname = et_nickname.getText().toString();
        String username = et_username.getText().toString();
        String username_back = et_username_back.getText().toString();//邮箱后缀
        String email = username + "@stu.edu.cn";//完整的邮箱
        String password1 = et_password_reg.getText().toString();
        String password2 = et_password2_reg.getText().toString();

        FormBody body = new FormBody.Builder()
                .add("email", email)
                .add("nickname", nickname)
                .add("password1", password1)
                .add("password2", password2)
                .add("request_type", "sign_up")
                .build();

        //发送请求
        OkHttpUtil.sendLoginOkHttpPostRequest(url, body,
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
                            Boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("test", "注册解析JSON异常");
                        }

                    }
                });
    }

}
