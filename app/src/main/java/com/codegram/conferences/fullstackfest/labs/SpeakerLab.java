package com.codegram.conferences.fullstackfest.labs;

import android.content.Context;

import com.codegram.conferences.fullstackfest.models.Speaker;

import java.util.ArrayList;

/**
 * Created by marc on 1/30/15.
 */
public class SpeakerLab {
    private ArrayList<Speaker> mSpeakers;
    private static SpeakerLab sSpeakerLab;
    private Context mAppContext;

    private SpeakerLab(Context appContext) {
        mAppContext = appContext;
        mSpeakers = new ArrayList<Speaker>();

        Speaker speaker = new Speaker(1, "Aaron Patterson");
        mSpeakers.add(speaker);

        speaker = new Speaker(2, "Sandi Metz");
        mSpeakers.add(speaker);
    }

    public static SpeakerLab get(Context context) {
        if (sSpeakerLab == null)
            sSpeakerLab = new SpeakerLab(context.getApplicationContext());
        return sSpeakerLab;
    }

    public ArrayList<Speaker> getSpeakers() {
        return mSpeakers;
    }

    public Speaker getSpeaker(int id) {
        for (Speaker talk : mSpeakers) {
            if (talk.getId() == id)
                return talk;
        }
        return null;
    }
}
