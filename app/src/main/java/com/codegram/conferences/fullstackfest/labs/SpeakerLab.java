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

        Speaker speaker = new Speaker(1, 1, "Aaron Patterson", "http://conferences.codegram.com/assets/fallback/speaker_default_picture-1da798ebd0ccbc5fbc49c9efd76c5b37.jpg", "bio 1");
        mSpeakers.add(speaker);

        speaker = new Speaker(2, 2, "Sandi Metz", "http://conferences.codegram.com/assets/fallback/speaker_default_picture-1da798ebd0ccbc5fbc49c9efd76c5b37.jpg", "bio 2");
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

    public Speaker getSpeakerByTalkId(int talkId) {
        for (Speaker speaker : mSpeakers) {
            if (speaker.getTalkId() == talkId)
                return speaker;
        }
        return null;
    }

    public void setCollection(ArrayList<Speaker> speakers) {
        mSpeakers = speakers;
    }
}
