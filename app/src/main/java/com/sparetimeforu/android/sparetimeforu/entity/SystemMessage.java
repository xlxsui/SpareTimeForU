package com.sparetimeforu.android.sparetimeforu.entity;

import java.io.Serializable;

/**
 * Created by HQY on 2019/10/3.
 */

public class SystemMessage implements Serializable{
    public final static int Add_Friend_Message=0;//加好友的信息
    public final static int Errand_Message=1;//任务相关的信息
    public final static int Approve_Message=2;//点赞的信息
    public final static int Comment_Message=3;//点赞的信息

    private int type;
    private String content;//信息内容
    private String date;//信息日期
    private int post_id;//相关帖子的id
    private int post_type;//相关帖子的类型
    private String user_nickname;
    private String user_Email;
    private int is_first_time_show;

    public int getIs_first_time_show() {
        return is_first_time_show;
    }

    public void setIs_first_time_show(int is_first_time_show) {
        this.is_first_time_show = is_first_time_show;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_Email() {
        return user_Email;
    }

    public void setUser_Email(String user_Email) {
        this.user_Email = user_Email;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
