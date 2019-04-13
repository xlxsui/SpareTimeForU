package com.sparetimeforu.android.sparetimeforu.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jin on 2018/11/4.
 */

public class Errand {
    private UUID mUUID;
    private User mUser;
    private double mReward;
    private String mOrigin;
    private String mDestination;
    private String mCaption;
    private String[] mPictureURLs;

    public Errand() {
        mUUID = UUID.randomUUID();
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String[] getPictureURLs() {
        return mPictureURLs;
    }

    public void setPictureURLs(String[] pictureURLs) {
        mPictureURLs = pictureURLs;
    }

    private Date mDeadline;


    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }


    public double getReward() {
        return mReward;
    }

    public void setReward(double reward) {
        mReward = reward;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public Date getDeadline() {
        return mDeadline;
    }

    public void setDeadline(Date deadline) {
        mDeadline = deadline;
    }

    @Override
    public String toString() {
        return "Errand{" +
                "mUUID=" + mUUID +
                ", mUser=" + mUser +
                ", mReward=" + mReward +
                ", mOrigin='" + mOrigin + '\'' +
                ", mDestination='" + mDestination + '\'' +
                ", mCaption='" + mCaption + '\'' +
                ", mPictureURLs=" + Arrays.toString(mPictureURLs) +
                ", mDeadline=" + mDeadline +
                '}';
    }
}
