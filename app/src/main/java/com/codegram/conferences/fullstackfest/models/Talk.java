package com.codegram.conferences.fullstackfest.models;

import java.util.UUID;

/**
 * Created by marc on 1/23/15.
 */
public class Talk {
    private int mId;
    private String mTitle;
    private int mSpeakerId;
    private String mDescription;

    public Talk(int id, String title, String description, int speakerId) {
        mId = id;
        mTitle = title;
        mSpeakerId = speakerId;
        mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getSpeakerId() { return mSpeakerId; }

    public String getDescription() {
        return mDescription;
    }
}
