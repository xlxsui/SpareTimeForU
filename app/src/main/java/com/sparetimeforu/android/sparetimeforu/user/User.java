package com.sparetimeforu.android.sparetimeforu.user;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/4.
 *
 */

public class User {

    private String nick_name;
    private String signature;
    private String picUrl;
    private String favourable_rate;

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
