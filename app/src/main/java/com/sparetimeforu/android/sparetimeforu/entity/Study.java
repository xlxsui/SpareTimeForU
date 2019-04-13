package com.sparetimeforu.android.sparetimeforu.entity;

import java.util.Arrays;
import java.util.UUID;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/14.
 */

public class Study {
    private UUID mUUID;
    private User mUser;
    private boolean mState;
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

    public boolean isState() {
        return mState;
    }

    public void setState(boolean state) {
        mState = state;
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
        return "Study{" +
                "mUUID=" + mUUID +
                ", mUser=" + mUser +
                ", mState=" + mState +
                ", mCaption='" + mCaption + '\'' +
                ", mPictureURLs=" + Arrays.toString(mPictureURLs) +
                '}';
    }
}
