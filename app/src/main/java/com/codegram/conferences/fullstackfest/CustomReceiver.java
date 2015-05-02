package com.codegram.conferences.fullstackfest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codegram.conferences.fullstackfest.services.FetchDataService;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcriera on 4/8/15.
 */
public class CustomReceiver extends ParsePushBroadcastReceiver {
    @Override
    public void onPushReceive(Context context, Intent intent) {
        JSONObject pushData = null;
        try {
            pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
        } catch (JSONException e) {
            Log.e("CustomReceiver", "Unexpected JSONException when receiving push data: ", e);
        }

        if(pushData != null && pushData.has("customAction")) {
            Log.d("CustomReceiver", "custom action!");
            ParseAnalytics.trackAppOpenedInBackground(intent);
            FetchDataService fetchDataService = new FetchDataService();
            fetchDataService.startFetching(context);
        } else {
            super.onPushReceive(context, intent);
        }
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        ParseAnalytics.trackAppOpenedInBackground(intent);
        super.onPushOpen(context, intent);
    }
}
