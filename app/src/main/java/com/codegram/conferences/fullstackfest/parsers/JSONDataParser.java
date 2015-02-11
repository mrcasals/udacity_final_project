package com.codegram.conferences.fullstackfest.parsers;

import android.util.Log;

import com.codegram.conferences.fullstackfest.models.Speaker;
import com.codegram.conferences.fullstackfest.models.Talk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marc on 2/9/15.
 */
public class JSONDataParser {
    private final String LOG_TAG = JSONDataParser.class.getSimpleName();
    private ArrayList<Talk> mTalks;
    private ArrayList<Speaker> mSpeakers;
    private String mJsonString;
    private final String JSON_EMBEDDED = "_embedded";
    private final String JSON_TALKS = "talks";
    private final String JSON_SPEAKER = "speaker";
    private final String TALK_TITLE = "title";
    private final String TALK_DESCRIPTION = "description";
    private final String SPEAKER_NAME = "name";
    private final String SPEAKER_PICTURE = "picture_big";


    public JSONDataParser(String jsonString) {
        mJsonString = jsonString;
    }

    public ArrayList<Talk> getTalks() {
        return mTalks;
    }

    public ArrayList<Speaker> getSpeakers() {
        return mSpeakers;
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

        for(int i = 0; i < talks.length(); i++) {
            int speakerId = i + 1;
            String title;
            String description;

            JSONObject talk = talks.getJSONObject(i);
            title = talk.getString(TALK_TITLE);
            description = talk.getString(TALK_DESCRIPTION);

            Talk parsedTalk = new Talk(i + 1, title, description, speakerId);
            mTalks.add(parsedTalk);
        }
    }

    private void parseSpeakers(String jsonData)
            throws JSONException {

        JSONObject json = new JSONObject(jsonData);
        JSONObject embedded = json.getJSONObject(JSON_EMBEDDED);
        JSONArray talks = embedded.getJSONArray(JSON_TALKS);

        mSpeakers = new ArrayList<Speaker>();

        for(int i = 0; i < talks.length(); i++) {
            String name;
            String pictureUrl;

            JSONObject talk = talks.getJSONObject(i);
            JSONObject speaker = talk.getJSONObject(JSON_EMBEDDED).getJSONObject(JSON_SPEAKER);
            name = speaker.getString(SPEAKER_NAME);

            if(speaker.isNull(SPEAKER_PICTURE)) {
                pictureUrl = "http://conferences.codegram.com/assets/fallback/speaker_default_picture-1da798ebd0ccbc5fbc49c9efd76c5b37.jpg";
            } else {
                pictureUrl = speaker.getString(SPEAKER_PICTURE);
            }

            Speaker parsedSpeaker = new Speaker(i + 1, name, pictureUrl);
            mSpeakers.add(parsedSpeaker);
        }
    }
}
