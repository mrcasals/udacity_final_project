package com.codegram.conferences.fullstackfest.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.codegram.conferences.fullstackfest.config.FullStackFestConfig;
import com.codegram.conferences.fullstackfest.data.DatabaseContract;
import com.codegram.conferences.fullstackfest.labs.SpeakerLab;
import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.parsers.JSONDataParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class FetchDataService extends IntentService {
    private final String LOG_TAG = FetchDataService.class.getSimpleName();

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startFetching(Context context) {
        Intent intent = new Intent(context, FetchDataService.class);
        context.startService(intent);
    }

    public FetchDataService() {
        super("FetchDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            JSONDataParser parser = getData();
            TalkLab.get(this).setCollection(parser.getTalks());
            SpeakerLab.get(this).setCollection(parser.getSpeakers());
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    protected JSONDataParser getData() {
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

    private void addToDatabase(JSONDataParser parser) {
        cleanDB();
        addTalksToDatabase(parser);
        addSpeakersToDatabase(parser);
    }

    private void addTalksToDatabase(JSONDataParser parser) {
        Cursor cursor = this.getContentResolver().query(
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
            int inserted = this.getContentResolver().bulkInsert(DatabaseContract.TalkEntry.CONTENT_URI, talksCVArray);
            Log.d(LOG_TAG, "FetchDataTask Complete. " + inserted + " Talks Inserted");
        }
        cursor.close();
    }

    private void addSpeakersToDatabase(JSONDataParser parser) {
        Cursor cursor = this.getContentResolver().query(
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
            int inserted = this.getContentResolver().bulkInsert(DatabaseContract.SpeakerEntry.CONTENT_URI, speakersCVArray);
            Log.d(LOG_TAG, "FetchDataTask Complete. " + inserted + " Speakers Inserted");
        }
        cursor.close();
    }

    private void cleanDB() {
        this.getContentResolver().delete(
                DatabaseContract.TalkEntry.CONTENT_URI,
                null,
                null
        );
        this.getContentResolver().delete(
                DatabaseContract.SpeakerEntry.CONTENT_URI,
                null,
                null
        );
    }
}
