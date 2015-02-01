package com.codegram.conferences.fullstackfest.models;

/**
 * Created by marc on 1/30/15.
 */
public class Speaker {
    private int mId;
    private String mName;

    public Speaker(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
