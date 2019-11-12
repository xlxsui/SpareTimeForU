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
    public static String HOST = "http://wifi.xlxs.top";
    public static User sUser=null;// 一个全局变量user
    public static GlobalEventListener globalEventListener = null;
    public static String manager_username = "manager@stu.edu.cn";
    public static List<SystemMessage> systemMessages = null;
    public static float stfu_tool_bar_height=0;
}

