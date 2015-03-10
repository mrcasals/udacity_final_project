package com.codegram.conferences.fullstackfest.models;

import org.markdownj.MarkdownProcessor;

import java.util.UUID;

/**
 * Created by marc on 1/23/15.
 */
public class Talk {
    private int mId;
    private String mTitle;
    private int mSpeakerId;
    private String mDescription;
    private String[] mTags;

    public Talk(int id, String title, String description, int speakerId, String[] tags) {
        mId = id;
        mTitle = title;
        mSpeakerId = speakerId;
        mDescription = description;
        mTags = tags;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getSpeakerId() { return mSpeakerId; }

    public String getDescription() {
        return new MarkdownProcessor().markdown(mDescription);
    }

    public String[] getTags() {
        return mTags;
    }
}
