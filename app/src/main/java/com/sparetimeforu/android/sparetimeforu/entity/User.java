package com.sparetimeforu.android.sparetimeforu.entity;

import java.io.Serializable;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/4.
 *
 */

public class User implements Serializable{

    private String nick_name;
    private String signature;
    private String picUrl;
    private String favourable_rate;
    private String phone;
    private String sex;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFavourable_rate() {
        return favourable_rate;
    }

    public void setFavourable_rate(String favourable_rate) {
        this.favourable_rate = favourable_rate;
    }
}
