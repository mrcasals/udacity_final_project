package com.codegram.conferences.fullstackfest.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.models.Talk;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marc on 2/6/15.
 */
public class FetchDataTask extends AsyncTask<Void, Void, ArrayList<Talk>> {
    private final String LOG_TAG = FetchDataTask.class.getSimpleName();
    private Context mContext;

    public FetchDataTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<Talk> doInBackground(Void... params) {
        String url = "http://conferences.codegram.com/api/baruco2014/talks/";
        HttpGet get = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        String jsonData = null;
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            jsonData = EntityUtils.toString(entity);
            Log.v(LOG_TAG, "JSON data: " + jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            return parseTalks(jsonData);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Talk> talks) {
        TalkLab.get(mContext).setCollection(talks);
    }

    private ArrayList<Talk> parseTalks(String jsonData)
        throws JSONException {
        final String JSON_EMBEDDED = "_embedded";
        final String JSON_TALKS = "talks";
        final String TALK_TITLE = "title";
        final String TALK_DESCRIPTION = "description";

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
        return parsedTalks;
    }
}
