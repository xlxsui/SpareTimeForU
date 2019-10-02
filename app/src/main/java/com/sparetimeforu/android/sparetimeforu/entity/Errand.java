package com.sparetimeforu.android.sparetimeforu.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jin on 2018/11/4.
 */

public class Errand extends LitePalSupport implements Serializable {
//    private UUID mUUID;
//    private User mUser;
//    private double mReward;
//    private String mOrigin;
//    private String mDestination;
//    private String mCaption;
//    private String[] mPictureURLs;
//    private int mRelease_time;
//    private boolean is_accepted;//标注是否已经被接受
//
//    public boolean isIs_accepted() {
//        return is_accepted;
//    }
//
//    public void setIs_accepted(boolean is_accepted) {
//        this.is_accepted = is_accepted;
//    }
//
//    public int getmRelease_time() {
//        return mRelease_time;
//    }
//
//    public void setmRelease_time(int mRelease_time) {
//        this.mRelease_time = mRelease_time;
//    }
//
//    public Errand() {
//        mUUID = UUID.randomUUID();
//    }
//
//    public String getCaption() {
//        return mCaption;
//    }
//
//    public void setCaption(String caption) {
//        mCaption = caption;
//    }
//
//    public String[] getPictureURLs() {
//        return mPictureURLs;
//    }
//
//    public void setPictureURLs(String[] pictureURLs) {
//        mPictureURLs = pictureURLs;
//    }
//
//    private Date mDeadline;
//
//
//    public User getUser() {
//        return mUser;
//    }
//
//    public void setUser(User user) {
//        mUser = user;
//    }
//
//    public UUID getUUID() {
//        return mUUID;
//    }
//
//    public void setUUID(UUID UUID) {
//        mUUID = UUID;
//    }
//
//
//    public double getReward() {
//        return mReward;
//    }
//
//    public void setReward(double reward) {
//        mReward = reward;
//    }
//
//    public String getOrigin() {
//        return mOrigin;
//    }
//
//    public void setOrigin(String origin) {
//        mOrigin = origin;
//    }
//
//    public String getDestination() {
//        return mDestination;
//    }
//
//    public void setDestination(String destination) {
//        mDestination = destination;
//    }
//
//    public Date getDeadline() {
//        return mDeadline;
//    }
//
//    public void setDeadline(Date deadline) {
//        mDeadline = deadline;
//    }
//
//    @Override
//    public String toString() {
//        return "Errand{" +
//                "mUUID=" + mUUID +
//                ", mUser=" + mUser +
//                ", mReward=" + mReward +
//                ", mOrigin='" + mOrigin + '\'' +
//                ", mDestination='" + mDestination + '\'' +
//                ", mCaption='" + mCaption + '\'' +
//                ", mPictureURLs=" + Arrays.toString(mPictureURLs) +
//                ", mDeadline=" + mDeadline +
//                '}';
//    }

    private String user_Email;
    private String user_Avatar;
    private String user_Nickname;
    private String content;
    private String picture_url_1;
    private String picture_url_2;
    private String picture_url_3;
    private String orgin;
    private String destination;
    private int like_number;
    private int comment_number;
    private int id;
    private int is_deleted;
    private String release_time;
    private String end_time;
    private float money;
    private int evaluate;
    private int receiver_id;
    private int is_received;
    private int errand_id;

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

    public int getErrand_id() {
        return errand_id;
    }

    public void setErrand_id(int errand_id) {
        this.errand_id = errand_id;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
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

    public String getOrgin() {
        return orgin;
    }

    public void setOrgin(String orgin) {
        this.orgin = orgin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getLike_number() {
        return like_number;
    }

    public void setLike_number(int like_number) {
        this.like_number = like_number;
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

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(int evaluate) {
        this.evaluate = evaluate;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getIs_received() {
        return is_received;
    }

    public void setIs_received(int is_received) {
        this.is_received = is_received;
    }

    @Override
    public String toString() {
        return "content:"+this.content;
    }
}
