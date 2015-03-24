package com.codegram.conferences.fullstackfest.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

    public FetchDataTask(Context context) {
        mContext = context;
    }

    @Override
    protected JSONDataParser doInBackground(Void... params) {
        HttpGet get = new HttpGet(FullStackFestConfig.API_ENDPOINT);
        HttpClient client = new DefaultHttpClient();
        String jsonData;
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

        addToDatabase(parser);

        return parser;
    }

    @Override
    protected void onPostExecute(JSONDataParser parser) {
        TalkLab.get(mContext).setCollection(parser.getTalks());
        SpeakerLab.get(mContext).setCollection(parser.getSpeakers());
        Toast.makeText(mContext, "Updated!", Toast.LENGTH_SHORT).show();
    }

    private void addToDatabase(JSONDataParser parser) {
        cleanDB();
        addTalksToDatabase(parser);
        addSpeakersToDatabase(parser);
    }

    private void addTalksToDatabase(JSONDataParser parser) {
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
            int inserted = mContext.getContentResolver().bulkInsert(DatabaseContract.TalkEntry.CONTENT_URI, talksCVArray);
            Log.d(LOG_TAG, "FetchDataTask Complete. " + inserted + " Talks Inserted");
        }
        cursor.close();
    }

    private void addSpeakersToDatabase(JSONDataParser parser) {
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.SpeakerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        Log.d(LOG_TAG, "Speakers found: " + Integer.toString(cursor.getCount()));
        if(cursor.getCount() == 0) {
            ContentValues[] speakersCVArray = new ContentValues[parser.getSpeakersCV().size()];
            parser.getSpeakersCV().toArray(speakersCVArray);
            int inserted = mContext.getContentResolver().bulkInsert(DatabaseContract.SpeakerEntry.CONTENT_URI, speakersCVArray);
            Log.d(LOG_TAG, "FetchDataTask Complete. " + inserted + " Speakers Inserted");
        }
        cursor.close();
    }

    private void cleanDB() {
        mContext.getContentResolver().delete(
                DatabaseContract.TalkEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                DatabaseContract.SpeakerEntry.CONTENT_URI,
                null,
                null
        );
    }
}
