package com.sparetimeforu.android.sparetimeforu;

import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;
import com.sparetimeforu.android.sparetimeforu.entity.User;

import java.util.List;

import Listener.GlobalEventListener;
import cn.jpush.im.android.api.model.Message;

/**
 * SpareTimeForU
 * Created by Jin on 2019/5/9.
 * Email:17wjli6@stu.edu.cn
 */
public class STFUConfig {
    public static String HOST = "http://www.xlxs.top:5000";
    public static User sUser;// 一个全局变量user
    public static GlobalEventListener globalEventListener = null;
    public static String manager_username = "manager@stu.edu.cn";
    public static List<SystemMessage> systemMessages = null;
}
