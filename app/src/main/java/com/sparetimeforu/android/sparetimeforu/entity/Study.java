package com.sparetimeforu.android.sparetimeforu.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/14.
 */

public class Study extends LitePalSupport implements Serializable {
    private String user_Email;
    private String user_Avatar;
    private String user_Nickname;
    private String content;
    private String picture_url_1;
    private String picture_url_2;
    private String picture_url_3;
    private int comment_number;
    private int id;
    private int is_deleted;
    private String release_time;
    private int study_id;
    private int is_solved;

    public int getStudy_id() {
        return study_id;
    }

    public void setStudy_id(int study_id) {
        this.study_id = study_id;
    }

    public int  isIs_solved() {
        return is_solved;
    }

    public void setIs_solved(int  is_solved) {
        this.is_solved = is_solved;
    }



    public String getUser_Email() {
        return user_Email;
    }

    public void setUser_Email(String user_Email) {
        this.user_Email = user_Email;
    }

    public String getUser_Avatar() {
        return user_Avatar;
    }

    public void setUser_Avatar(String user_Avatar) {
        this.user_Avatar = user_Avatar;
    }

    public String getUser_Nickname() {
        return user_Nickname;
    }

    public void setUser_Nickname(String user_Nickname) {
        this.user_Nickname = user_Nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture_url_1() {
        return picture_url_1;
    }

    public void setPicture_url_1(String picture_url_1) {
        this.picture_url_1 = picture_url_1;
    }

    public String getPicture_url_2() {
        return picture_url_2;
    }

    public void setPicture_url_2(String picture_url_2) {
        this.picture_url_2 = picture_url_2;
    }

    public String getPicture_url_3() {
        return picture_url_3;
    }

    public void setPicture_url_3(String picture_url_3) {
        this.picture_url_3 = picture_url_3;
    }

    public int getComment_number() {
        return comment_number;
    }

    public void setComment_number(int comment_number) {
        this.comment_number = comment_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }



}
