package com.codegram.conferences.fullstackfest;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codegram.conferences.fullstackfest.config.FullStackFestConfig;

import com.codegram.conferences.fullstackfest.tasks.FetchDataTask;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class ListActivity extends MaterialNavigationDrawer {
    private final String LOG_TAG = ListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void init(Bundle savedInstanceState) {
        // Set custom background image for the header
        this.setDrawerHeaderImage(R.drawable.fsf);

        // Don't show the sidebar on app opening
        this.disableLearningPattern();

        // this adds an arrow animation to the drawer
        //this.allowArrowAnimation();

        // On back button, return to first section of the drawer. It's buggy right now:
        // https://github.com/neokree/MaterialNavigationDrawer/issues/181
        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        //create sections
        this.addSection(newSection("Talks", R.drawable.ic_action_today, new TalkTabsFragment()).setSectionColor(Color.parseColor(FullStackFestConfig.BARUCO_COLOR)));

        Intent mapIntent = createMapIntent();
        if(mapIntent.resolveActivity(getPackageManager()) != null) {
            this.addSection(newSection("Map", R.drawable.ic_action_map, mapIntent));
        } else {
            Log.d(LOG_TAG, "Couldn't call map intent, no map available");
        }

        Intent socialIntent = createSocialIntent();
        if(socialIntent.resolveActivity(getPackageManager()) != null) {
            this.addSection(newSection("Social", R.drawable.ic_action_forum, socialIntent));
        } else {
            Log.d(LOG_TAG, "Couldn't call browser intent, no twitter handler available");
        }
    }

    private Intent createMapIntent() {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        String location = FullStackFestConfig.LATLONG;
        String venueName = FullStackFestConfig.VENUE_NAME;
        Uri geolocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location + "(" + venueName + ")")
                .build();
        mapIntent.setData(geolocation);
        return mapIntent;
    }

    private Intent createSocialIntent() {
        Uri twitterHashtag = Uri.parse("https://twitter.com/search?q=" + FullStackFestConfig.HASHTAG);
        return new Intent(Intent.ACTION_VIEW, twitterHashtag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_talks_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                updateRemoteData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateRemoteData() {
        FetchDataTask fetchDataTask = new FetchDataTask(this);
        fetchDataTask.execute();
    }
}
