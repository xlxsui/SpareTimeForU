package com.sparetimeforu.android.sparetimeforu.ServerConnection;


import android.util.Log;

import com.sparetimeforu.android.sparetimeforu.user.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;

/**
 * Created by HQY on 2018/11/14.
 */

public class OkHttpUtil {


    /*
    ** 发送数据到服务器，获取登陆数据
     */
    public static void sendLoginOkHttpGetRequest(String address, Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


    public static void sendLoginOkHttpPostRequest(String url, FormBody body,Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
