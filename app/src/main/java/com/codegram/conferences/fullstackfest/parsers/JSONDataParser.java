package com.codegram.conferences.fullstackfest.parsers;

import android.content.ContentValues;
import android.util.Log;

import com.codegram.conferences.fullstackfest.data.DatabaseContract.TalkEntry;
import com.codegram.conferences.fullstackfest.data.DatabaseContract.SpeakerEntry;
import com.codegram.conferences.fullstackfest.models.Speaker;
import com.codegram.conferences.fullstackfest.models.Talk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by marc on 2/9/15.
 */
public class JSONDataParser {
    private final String LOG_TAG = JSONDataParser.class.getSimpleName();
    private ArrayList<Talk> mTalks;
    private ArrayList<Speaker> mSpeakers;
    private Vector<ContentValues> mTalksCV;
    private Vector<ContentValues> mSpeakersCV;
    private String mJsonString;
    private final String JSON_EMBEDDED = "_embedded";
    private final String JSON_TALKS = "talks";
    private final String JSON_SPEAKER = "speaker";
    private final String TALK_TITLE = "title";
    private final String TALK_DESCRIPTION = "description";
    private final String TALK_PICTURE = "picture";
    private final String TALK_URL = "url";
    private final String TALK_TAGS = "tag_titles";
    private final String SPEAKER_NAME = "name";
    private final String SPEAKER_PICTURE = "picture_big";
    private final String SPEAKER_BIO = "bio";


    public JSONDataParser(String jsonString) {
        mJsonString = jsonString;
    }

    public ArrayList<Talk> getTalks() {
        return mTalks;
    }

    public ArrayList<Speaker> getSpeakers() {
        return mSpeakers;
    }

    public Vector<ContentValues> getTalksCV() {
        return mTalksCV;
    }

    public Vector<ContentValues> getSpeakersCV() {
        return mSpeakersCV;
    }

    public boolean parse() {
        try {
            parseTalks(mJsonString);
            parseSpeakers(mJsonString);
            return true;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }

    private void parseTalks(String jsonData)
            throws JSONException {

        JSONObject json = new JSONObject(jsonData);
        JSONObject embedded = json.getJSONObject(JSON_EMBEDDED);
        JSONArray talks = embedded.getJSONArray(JSON_TALKS);

        mTalks = new ArrayList<Talk>();
        mTalksCV = new Vector<ContentValues>(talks.length());

        for(int i = 0; i < talks.length(); i++) {
            String title;
            String description;
            String[] tags;

            JSONObject talk = talks.getJSONObject(i);
            title = talk.getString(TALK_TITLE);
            description = talk.getString(TALK_DESCRIPTION);
            JSONArray talkTags =  talk.getJSONArray(TALK_TAGS);
            tags = new String[talkTags.length()];

            for(int j=0;j<talkTags.length();j++)
                tags[j]=talkTags.getString(j);

            Talk parsedTalk = new Talk(i + 1, title, description, tags);
            mTalksCV.add(createCVFromTalk(parsedTalk));
            mTalks.add(parsedTalk);
        }
    }

    private void parseSpeakers(String jsonData)
            throws JSONException {

        JSONObject json = new JSONObject(jsonData);
        JSONObject embedded = json.getJSONObject(JSON_EMBEDDED);
        JSONArray talks = embedded.getJSONArray(JSON_TALKS);

        mSpeakers = new ArrayList<Speaker>();
        mSpeakersCV = new Vector<ContentValues>(talks.length());

        for(int i = 0; i < talks.length(); i++) {
            String name;
            String pictureUrl;
            String bio;

            JSONObject talk = talks.getJSONObject(i);
            JSONObject speaker = talk.getJSONObject(JSON_EMBEDDED).getJSONObject(JSON_SPEAKER);
            name = speaker.getString(SPEAKER_NAME);

            if(speaker.isNull(SPEAKER_PICTURE)) {
                pictureUrl = talk.getJSONObject(TALK_PICTURE).getJSONObject(TALK_PICTURE).getString(TALK_URL);
            } else {
                pictureUrl = speaker.getString(SPEAKER_PICTURE);
            }

            bio = speaker.getString(SPEAKER_BIO);

            Speaker parsedSpeaker = new Speaker(i + 1, i + 1, name, pictureUrl, bio);
            mSpeakersCV.add(createCVFromSpeaker(parsedSpeaker));
            mSpeakers.add(parsedSpeaker);
        }
    }

    private ContentValues createCVFromTalk(Talk talk) {
        ContentValues talkValues = new ContentValues();
        talkValues.put(TalkEntry.COLUMN_TITLE, talk.getTitle());
        talkValues.put(TalkEntry.COLUMN_DESCRIPTION, talk.getDescription());

        return talkValues;
    }

    private ContentValues createCVFromSpeaker(Speaker speaker) {
        ContentValues speakerValues = new ContentValues();
        speakerValues.put(SpeakerEntry.COLUMN_NAME, speaker.getName());
        speakerValues.put(SpeakerEntry.COLUMN_BIO, speaker.getBio());
        speakerValues.put(SpeakerEntry.COLUMN_PHOTO_URL, speaker.getPictureUrl());
        speakerValues.put(SpeakerEntry.COLUMN_TALK_ID, speaker.getTalkId());

        return speakerValues;
    }
}
