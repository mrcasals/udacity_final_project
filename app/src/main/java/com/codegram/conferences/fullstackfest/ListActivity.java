package com.codegram.conferences.fullstackfest;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.codegram.conferences.fullstackfest.config.FullStackFestConfig;

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
        this.addSection(newSection("Schedule", new TalkTabsFragment()).setSectionColor(Color.parseColor(FullStackFestConfig.BARUCO_COLOR)));
        this.addSection(newSection("Speakers", new SpeakerListFragment()).setSectionColor(Color.parseColor(FullStackFestConfig.FUTUREJS_COLOR)));

        // ---------------------------------------------------
        // set a subheader
        this.addSubheader("Subheader");

        //create sections
        this.addSection(newSection("Sponsors", new TalkListFragment()).setSectionColor(Color.parseColor("#cddc39")));

        Intent mapIntent = createMapIntent();
        if(mapIntent.resolveActivity(getPackageManager()) != null) {
            this.addSection(newSection("Map", mapIntent).setSectionColor(Color.parseColor("#ff9800")));
        } else {
            Log.d(LOG_TAG, "Couldn't call map intent, no map available");
        }

        this.addBottomSection(newSection("About", new SpeakerListFragment()).setSectionColor(Color.parseColor("#455A64")));
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
}
