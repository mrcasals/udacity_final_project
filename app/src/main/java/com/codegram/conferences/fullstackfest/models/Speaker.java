package com.codegram.conferences.fullstackfest.models;

import org.markdownj.MarkdownProcessor;

/**
 * Created by marc on 1/30/15.
 */
public class Speaker {
    private int mId;
    private String mName;
    private String mPictureUrl;
    private String mBio;
    private int mTalkId;

    public Speaker(int id, int talkId, String name, String pictureUrl, String bio) {
        mId = id;
        mTalkId = talkId;
        mName = name;
        mPictureUrl = pictureUrl;
        mBio = bio;
    }

    public int getTalkId() {
        return mTalkId;
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
        return new MarkdownProcessor().markdown(mBio);
    }
}
