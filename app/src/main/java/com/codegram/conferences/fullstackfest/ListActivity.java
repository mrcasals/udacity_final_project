package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
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
        this.addSection(newSection("Talks", new TalkTabsFragment()).setSectionColor(Color.parseColor(FullStackFestConfig.BARUCO_COLOR)));
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
