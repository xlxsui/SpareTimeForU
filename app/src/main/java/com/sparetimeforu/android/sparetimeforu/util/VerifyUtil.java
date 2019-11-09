package com.sparetimeforu.android.sparetimeforu.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * 用来验证的工具，如用户是否登录，令牌有没有失效
 * Created by Jin on 2019/10/21.
 * Email:17wjli6@stu.edu.cn
 */
public class VerifyUtil {

    /**
     * 验证用户是否登录，登录返回true，否则打开登录界面
     *
     * @param context 用来打开LoginActivity的上下文
     * @return 是否登录
     */
    public static boolean isLogin(Context context) {
        if (STFUConfig.sUser != null) {
            if (STFUConfig.sUser.getUser_id() != 0
                    && STFUConfig.sUser.getEmail() != null && isTokenValid(context)) {
                return true;
            }
        }
        ((Activity) context).runOnUiThread(() -> Toast.makeText(context, R.string.login_request, Toast.LENGTH_SHORT).show());
        login(context);
        return false;
    }

    /**
     * 验证用户是否登录，登录返回true
     *
     * @param context 用来打开LoginActivity的上下文
     * @return 是否登录
     */
    public static boolean isLoginStatus(Context context) {
        if (STFUConfig.sUser != null) {
            if (STFUConfig.sUser.getUser_id() != 0
                    && STFUConfig.sUser.getEmail() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户的令牌是否过期或者在其它设备登录
     *
     * @param context 用来打开LoginActivity的上下文
     * @return 结果
     */
    public static boolean isTokenValid(Context context) {
        if (STFUConfig.sUser != null) {
            if (STFUConfig.sUser.getAuth_token() == null) {
                return false;
            }
            FormBody body = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .build();

            OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/auth/is_token_invalid", body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    login(context);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String s = jsonObject.getString("status");
                        if (!s.equals("success")) {
                            ((Activity) context).runOnUiThread(() -> Toast.makeText(context, R.string.login_expire, Toast.LENGTH_SHORT).show());
                            login(context);
                        }
                    } catch (JSONException e) {
                        Logger.e(e.toString());
                        login(context);
                    }
                }
            });
            return true;
        } else {
            return false;
        }
    }

    public static void login(Context context) {
        STFUConfig.sUser = null;
        ((Activity) context).runOnUiThread(() -> {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        });
    }
}
