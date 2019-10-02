package com.sparetimeforu.android.sparetimeforu;

import com.sparetimeforu.android.sparetimeforu.entity.User;

import Listener.GlobalEventListener;

/**
 * SpareTimeForU
 * Created by Jin on 2019/5/9.
 * Email:17wjli6@stu.edu.cn
 */
public class STFUConfig {
    public static String HOST = "http://www.xlxs.top:5000";
    public static User sUser;// 一个全局变量user
    public static GlobalEventListener globalEventListener=new GlobalEventListener();
    public static String manager_username="";
}
