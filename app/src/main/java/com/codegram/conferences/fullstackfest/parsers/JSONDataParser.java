package com.codegram.conferences.fullstackfest.parsers;

import android.util.Log;

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
    private String mJsonString;
    private final String JSON_EMBEDDED = "_embedded";
    private final String JSON_TALKS = "talks";
    private final String TALK_TITLE = "title";
    private final String TALK_DESCRIPTION = "description";


    public JSONDataParser(String jsonString) {
        mJsonString = jsonString;
    }

    public ArrayList<Talk> getTalks() {
        return mTalks;
    }

    public boolean parse() {
        try {
            parseTalks(mJsonString);
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

        ArrayList<Talk> parsedTalks = new ArrayList<Talk>();

        for(int i = 0; i < talks.length(); i++) {
            int speakerId = (i % 2) + 1;
            String title;
            String description;

            JSONObject talk = talks.getJSONObject(i);
            title = talk.getString(TALK_TITLE);
            description = talk.getString(TALK_DESCRIPTION);

            Talk parsedTalk = new Talk(i + 1, title, description, speakerId);
            parsedTalks.add(parsedTalk);
        }
        mTalks = parsedTalks;
    }


}
