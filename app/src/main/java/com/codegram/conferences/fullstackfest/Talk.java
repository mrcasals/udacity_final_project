package com.codegram.conferences.fullstackfest;

import java.util.UUID;

/**
 * Created by marc on 1/23/15.
 */
public class Talk {
    private UUID mId;
    private String mTitle;

    public Talk() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
