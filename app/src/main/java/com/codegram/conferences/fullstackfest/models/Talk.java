package com.codegram.conferences.fullstackfest.models;

import org.markdownj.MarkdownProcessor;

import java.util.UUID;

/**
 * Created by marc on 1/23/15.
 */
public class Talk {
    private int mId;
    private String mTitle;
    private String mDescription;
    private String[] mTags;

    public Talk(int id, String title, String description, String[] tags) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mTags = tags;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return new MarkdownProcessor().markdown(mDescription);
    }

    public String[] getTags() {
        return mTags;
    }
}
