package com.sparetimeforu.android.sparetimeforu.util;

import com.google.gson.Gson;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;


import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * 用来发送系统消息的类
 * Created by HQY on 2019/10/3.
 */

public class SystemMessageSendUtil {
//    username代表要发送信息的对象用户
    private final static int Errand_message_mode_errand_accepted=0;//任务被接受
    private final static int Errand_message_mode_errand_solved=1;//任务被完成
    public static void  send_System_message_add_friend(String username){
        JMessageClient.login(STFUConfig.manager_username, STFUConfig.manager_username + "1", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                //登陆管理员账号,发送添加好友信息
                if(i==0){
                    //创建System_message
                    Date dNow = new Date( );
                    SimpleDateFormat ft =
                            new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                    SystemMessage systemMessage=new SystemMessage();
                    systemMessage.setDate(ft.format(dNow));
                    systemMessage.setIs_first_time_show(1);//未读消息
                    systemMessage.setType(SystemMessage.Add_Friend_Message);
                    systemMessage.setContent("想加您为好友");
                    systemMessage.setUser_Email(STFUConfig.sUser.getEmail());//代表此消息的源头用户
                    systemMessage.setUser_nickname(STFUConfig.sUser.getNickname());
                    JMessageClient.createSingleTextMessage(username,null,get_Formate_Content(systemMessage));
                }
            }
        });
    }
    public static void  send_System_message_comment(String username,String comment_string,int post_id,int post_type){
        JMessageClient.login(STFUConfig.manager_username, STFUConfig.manager_username + "1", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                //登陆管理员账号,发送添加好友信息
                if(i==0){
                    //创建System_message
                    Date dNow = new Date( );
                    SimpleDateFormat ft =
                            new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                    SystemMessage systemMessage=new SystemMessage();
                    systemMessage.setDate(ft.format(dNow));
                    systemMessage.setIs_first_time_show(1);//未读消息
                    systemMessage.setPost_type(post_type);
                    systemMessage.setPost_id(post_id);
                    systemMessage.setType(SystemMessage.Comment_Message);
                    systemMessage.setContent(comment_string);
                    systemMessage.setUser_Email(STFUConfig.sUser.getEmail());//代表此消息的源头用户
                    systemMessage.setUser_nickname(STFUConfig.sUser.getNickname());
                    JMessageClient.createSingleTextMessage(username,null,get_Formate_Content(systemMessage));
                }
            }
        });
    }
    public static void  send_System_message_approve(String username,int post_id,int post_type){
        JMessageClient.login(STFUConfig.manager_username, STFUConfig.manager_username + "1", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                //登陆管理员账号,发送添加好友信息
                if(i==0){
                    //创建System_message
                    Date dNow = new Date( );
                    SimpleDateFormat ft =
                            new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                    SystemMessage systemMessage=new SystemMessage();
                    systemMessage.setDate(ft.format(dNow));
                    systemMessage.setIs_first_time_show(1);//未读消息
                    systemMessage.setType(SystemMessage.Approve_Message);
                    systemMessage.setPost_id(post_id);
                    systemMessage.setPost_type(post_type);
                    systemMessage.setContent("给您的帖子点了赞");
                    systemMessage.setUser_Email(STFUConfig.sUser.getEmail());//代表此消息的源头用户
                    systemMessage.setUser_nickname(STFUConfig.sUser.getNickname());
                    JMessageClient.createSingleTextMessage(username,null,get_Formate_Content(systemMessage));
                }
            }
        });
    }
    public static void  send_System_message_errand(String username,int post_id,int post_type,int mode){
        JMessageClient.login(STFUConfig.manager_username, STFUConfig.manager_username + "1", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                //登陆管理员账号,发送添加好友信息
                if(i==0){
                    //创建System_message
                    Date dNow = new Date( );
                    SimpleDateFormat ft =
                            new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                    SystemMessage systemMessage=new SystemMessage();
                    systemMessage.setDate(ft.format(dNow));
                    systemMessage.setIs_first_time_show(1);//未读消息
                    systemMessage.setType(SystemMessage.Approve_Message);
                    systemMessage.setPost_id(post_id);
                    systemMessage.setPost_type(post_type);
                    switch (mode){//根据类型来设置发送的消息
                        case SystemMessageSendUtil.Errand_message_mode_errand_accepted:
                            systemMessage.setContent("接受了您的任务");
                            break;
                        case SystemMessageSendUtil.Errand_message_mode_errand_solved:
                            systemMessage.setContent("完成了您的任务");
                            break;
                    }
                    systemMessage.setUser_Email(STFUConfig.sUser.getEmail());//代表此消息的源头用户
                    systemMessage.setUser_nickname(STFUConfig.sUser.getNickname());
                    Message message=JMessageClient.createSingleTextMessage(username,null,get_Formate_Content(systemMessage));
                    JMessageClient.sendMessage(message);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            //发送系统通知完成，重新登陆用户账号
                            JMessageClient.login(STFUConfig.sUser.getEmail(), STFUConfig.sUser.getEmail() + "1", new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {

                                }
                            });
                        }
                    });
                }
            }
        });
    }
    public static String get_Formate_Content(SystemMessage systemMessage){
        if(systemMessage!=null){
            Gson gson=new Gson();
            return gson.toJson(systemMessage);
        }
        return null;
    }
}
