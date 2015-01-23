package com.codegram.conferences.fullstackfest;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by marc on 1/23/15.
 */
public class TalkLab {
    private ArrayList<Talk> mTalks;
    private static TalkLab sTalkLab;
    private Context mAppContext;

    private TalkLab(Context appContext) {
        mAppContext = appContext;
        mTalks = new ArrayList<Talk>();

        Talk talk = new Talk();
        talk.setTitle("Inspecting ActiveRecord model relations with d3.js");
        mTalks.add(talk);

        talk = new Talk();
        talk.setTitle("Awesome talk");
        mTalks.add(talk);
    }

    public static TalkLab get(Context context) {
        if (sTalkLab == null)
            sTalkLab = new TalkLab(context.getApplicationContext());
        return sTalkLab;
    }

    public ArrayList<Talk> getTalks() {
        return mTalks;
    }

    public Talk getTalk(UUID id) {
        for (Talk talk : mTalks) {
            if (talk.getId().equals(id))
                return talk;
        }
        return null;
    }
}
