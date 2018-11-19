package com.sparetimeforu.android.sparetimeforu.util;

import android.text.TextUtils;

import com.sparetimeforu.android.sparetimeforu.user.User;

import org.json.JSONObject;

/**
 * Created by HQY on 2018/11/15.
 */

public class HandleMessageUtil {

    /*
    **  解析返回的登陆信息用户数据
     */

    public static User handleLoginMessage(String response){
        if(!TextUtils.isEmpty(response)){
            try{



                JSONObject object1=new JSONObject(response);
                Boolean success=object1.getBoolean("success");
                if(success) {
                    JSONObject userObject = new JSONObject(object1.getString("data"));
                    User user = new User();
                    user.setNick_name(userObject.getString("nickname"));
                    user.setSignature(userObject.getString("personal_signate"));
                    user.setFavourable_rate(userObject.getString("favourable_rate"));
                    user.setPicUrl(userObject.getString("photo_location"));

                    return user;

                }else return null;

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
