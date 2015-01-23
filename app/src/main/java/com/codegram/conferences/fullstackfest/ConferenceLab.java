package com.codegram.conferences.fullstackfest;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by marc on 1/23/15.
 */
public class ConferenceLab {
    private ArrayList<Conference> mConferences;
    private static ConferenceLab sConferenceLab;
    private Context mAppContext;

    private ConferenceLab(Context appContext) {
        mAppContext = appContext;
        mConferences = new ArrayList<Conference>();

        Conference conference = new Conference();
        conference.setTitle("Barcelona Ruby Conference");
        mConferences.add(conference);

        conference = new Conference();
        conference.setTitle("FutureJS");
        mConferences.add(conference);
    }

    public static ConferenceLab get(Context context) {
        if (sConferenceLab == null)
            sConferenceLab = new ConferenceLab(context.getApplicationContext());
        return sConferenceLab;
    }

    public ArrayList<Conference> getConferences() {
        return mConferences;
    }

    public Conference getConference(UUID id) {
        for (Conference conference : mConferences) {
            if (conference.getId().equals(id))
                return conference;
        }
        return null;
    }
}
