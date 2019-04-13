package com.sparetimeforu.android.sparetimeforu.ServerConnection;


import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by HQY on 2018/11/14.
 */

public class OkHttpUtil {


    /*
    ** 发送数据到服务器，获取登陆数据, get方法基本不用
     */
    public static void sendOkHttpGetRequest(String address, Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpPostRequest(String url, FormBody body, Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
