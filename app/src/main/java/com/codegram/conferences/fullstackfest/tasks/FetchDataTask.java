package com.codegram.conferences.fullstackfest.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.codegram.conferences.fullstackfest.config.FullStackFestConfig;
import com.codegram.conferences.fullstackfest.data.DatabaseContract;
import com.codegram.conferences.fullstackfest.labs.SpeakerLab;
import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.models.Talk;
import com.codegram.conferences.fullstackfest.parsers.JSONDataParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by marc on 2/6/15.
 */
public class FetchDataTask extends AsyncTask<Void, Void, JSONDataParser> {
    private final String LOG_TAG = FetchDataTask.class.getSimpleName();
    private Context mContext;
    private ArrayAdapter<Talk> mAdapter;

    public FetchDataTask(Context context, ArrayAdapter<Talk> adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    @Override
    protected JSONDataParser doInBackground(Void... params) {
        HttpGet get = new HttpGet(FullStackFestConfig.API_ENDPOINT);
        HttpClient client = new DefaultHttpClient();
        String jsonData = null;
        JSONDataParser parser;
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            jsonData = EntityUtils.toString(entity);
            Log.v(LOG_TAG, "JSON data: " + jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        parser = new JSONDataParser(jsonData);
        parser.parse();
        return parser;
    }

    @Override
    protected void onPostExecute(JSONDataParser parser) {
        TalkLab.get(mContext).setCollection(parser.getTalks());
        SpeakerLab.get(mContext).setCollection(parser.getSpeakers());
        mAdapter.clear();
        mAdapter.addAll(parser.getTalks());

        int inserted = 0;
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.TalkEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        Log.d(LOG_TAG, "Talks found: " + Integer.toString(cursor.getCount()));
        if(cursor.getCount() == 0) {
            ContentValues[] talksCVArray = new ContentValues[parser.getTalksCV().size()];
            parser.getTalksCV().toArray(talksCVArray);
            inserted = mContext.getContentResolver().bulkInsert(DatabaseContract.TalkEntry.CONTENT_URI, talksCVArray);
            Log.d(LOG_TAG, "FetchDataTask Complete. " + inserted + " Talks Inserted");
        }
    }
}
