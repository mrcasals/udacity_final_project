package com.codegram.conferences.fullstackfest;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by marcriera on 4/8/15.
 */
public class MyApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    }
}
