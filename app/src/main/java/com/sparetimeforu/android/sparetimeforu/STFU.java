package com.sparetimeforu.android.sparetimeforu;

import android.app.Application;

import com.sparetimeforu.android.sparetimeforu.entity.User;

/**
 * SpareTimeForU
 * Created by Jin on 2019/5/9.
 * Email:17wjli6@stu.edu.cn
 */
public class STFU extends Application {
    //    public static String HOST = "http://10.21.136.3:5000";
    private String HOST = "http://www.xlxs.top:5000";
    //    public static String HOST = "http://10.0.2.2:5000";//模拟器
    private User User;// 一个全局变量user

    @Override
    public void onCreate() {
        super.onCreate();
        User = new User();//new好，指向一个内存
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        this.User = user;
    }


}
