package com.sparetimeforu.android.sparetimeforu.recycler.view.item;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jin on 2018/11/4.
 */

public class Errand {
    private UUID mUUID;
    private String mName;
    private double mReward;
    private String mOrigin;
    private String mDestination;
    private Date mDeadline;

    public Errand() {
        //nothing to do now
        mUUID = UUID.randomUUID();
    }


    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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
}
