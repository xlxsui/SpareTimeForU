package com.sparetimeforu.android.sparetimeforu.entity;

import java.util.Arrays;
import java.util.UUID;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/15.
 */

public class IdleThing {
    private UUID mUUID;
    private User mUser;
    private double mPrice;
    private String mCaption;
    private String[] mPictureURLs;

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
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

    @Override
    public String toString() {
        return "IdleThing{" +
                "mUUID=" + mUUID +
                ", mUser=" + mUser +
                ", mPrice=" + mPrice +
                ", mCaption='" + mCaption + '\'' +
                ", mPictureURLs=" + Arrays.toString(mPictureURLs) +
                '}';
    }
}
