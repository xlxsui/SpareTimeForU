package com.sparetimeforu.android.sparetimeforu.util;

import android.text.TextUtils;

import com.sparetimeforu.android.sparetimeforu.entity.User;

import org.json.JSONObject;

import com.orhanobut.logger.Logger;

/**
 * Created by HQY on 2018/11/15.
 */

public class HandleMessageUtil {

    /*
    **  解析返回的登陆信息用户数据
     */

    public static User handleLoginMessage(String response) {
        Logger.i(response);
        if (!TextUtils.isEmpty(response)) {
            try {


                JSONObject object1 = new JSONObject(response);
                int success = object1.getInt("success");
                Logger.i(response);
                if (success == 1) {
                    JSONObject userObject = new JSONObject(object1.getString("data"));//
                    User user = new User();
                    user.setNick_name(userObject.getString("nickname"));
                    user.setSignature(userObject.getString("signature"));
                    // user.setFavourable_rate(userObject.getString("favourable_rate"));
                    user.setPicUrl(userObject.getString("avatar_url"));
                    user.setSex(userObject.getString("gender"));
                    user.setPhone(userObject.getString("phone"));
                    user.setEmail(userObject.getString("email"));

                    return user;
                } else return null;
            } catch (Exception e) {
                Logger.i(e.getMessage());
            }

        }
        return null;
    }
}
