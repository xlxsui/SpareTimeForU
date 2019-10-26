package com.sparetimeforu.android.sparetimeforu.entity;

import java.io.Serializable;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class Comment implements Serializable {
    //{"content":"恍惚迷离重叙旧缘","
    // disabled":0,"
    // id":3,"post_id":502,
    // "time":"Thu, 03 Oct 2019 06:34:34 GMT",
    // "user_avatar":"default.png",
    // "user_email":"17qyhu@stu.edu.cn",
    // "user_id":17,
    // "user_nickname":"Yuan"}
    private int id;
    private String content;
    private String time;
    private int disabled;//判断此评论是否被屏蔽
    private int post_id;//该评论对应的帖子id
    private int user_id;//该评论对应的用户id
    private String user_avatar;
    private String user_email;
    private String user_nickname;
    private String post_type;

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", disabled=" + disabled +
                ", post_id=" + post_id +
                ", user_id=" + user_id +
                ", user_avatar='" + user_avatar + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                '}';
    }
}
