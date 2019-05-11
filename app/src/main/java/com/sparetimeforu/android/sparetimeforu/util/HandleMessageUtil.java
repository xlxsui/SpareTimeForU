package com.sparetimeforu.android.sparetimeforu.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.sparetimeforu.android.sparetimeforu.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import com.orhanobut.logger.Logger;

/**
 * Created by HQY on 2018/11/15.
 */

public class HandleMessageUtil {

    /**
     * 解析返回的登陆信息用户数据
     *
     * @param responseString
     * @return
     */
    public static User handleLoginMessage(String responseString) {
        Logger.i(responseString);
        if (!TextUtils.isEmpty(responseString)) {
            Gson gson = new Gson();
            User user = null;
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                user = gson.fromJson(jsonObject.getString("data"), User.class);
            } catch (JSONException e) {
                Logger.e(e.toString());
            }
            return user;
        }
        return null;
    }
}
