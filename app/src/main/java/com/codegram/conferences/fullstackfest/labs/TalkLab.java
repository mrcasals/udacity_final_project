package com.codegram.conferences.fullstackfest.labs;

import android.content.Context;

import com.codegram.conferences.fullstackfest.models.Talk;

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

        Talk talk = new Talk(1, "Inspecting ActiveRecord model relations with d3.js", "DEsc 1", new String[] {"ruby"});
        mTalks.add(talk);

        talk = new Talk(2, "Awesome talk", "Desc 2", new String[] {"javascript"});
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

    public ArrayList<Talk> getTalksByTag(int module) {
        ArrayList<Talk> talksByTag = new ArrayList<Talk>();
        for (Talk talk : mTalks) {
            if (talk.getId() % 2 == module)
                talksByTag.add(talk);
        }
        return talksByTag;
    }

    public Talk getTalk(int id) {
        for (Talk talk : mTalks) {
            if (talk.getId() == id)
                return talk;
        }
        return null;
    }

    public void setCollection(ArrayList<Talk> talks) {
        mTalks = talks;
    }
}
