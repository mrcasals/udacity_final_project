package com.codegram.conferences.fullstackfest.models;

/**
 * Created by marc on 1/30/15.
 */
public class Speaker {
    private int mId;
    private String mName;
    private String mPictureUrl;
    private String mBio;

    public Speaker(int id, String name, String pictureUrl, String bio) {
        mId = id;
        mName = name;
        mPictureUrl = pictureUrl;
        mBio = bio;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public String getBio() {
        return mBio;
    }
}
