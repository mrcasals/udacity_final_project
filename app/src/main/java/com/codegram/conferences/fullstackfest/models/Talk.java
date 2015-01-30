package com.codegram.conferences.fullstackfest.models;

import java.util.UUID;

/**
 * Created by marc on 1/23/15.
 */
public class Talk {
    private int mId;
    private String mTitle;
    private int mSpeakerId;

    public Talk(int id, String title, int speakerId) {
        mId = id;
        mTitle = title;
        mSpeakerId = speakerId;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getSpeakerId() { return mSpeakerId; }
}
