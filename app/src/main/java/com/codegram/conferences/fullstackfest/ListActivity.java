package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.os.Bundle;

import com.codegram.conferences.fullstackfest.config.FullStackFestConfig;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class ListActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        // Set custom background image for the header
        this.setDrawerHeaderImage(R.drawable.fsf);

        // Don't show the sidebar on app opening
        this.disableLearningPattern();

        // this adds an arrow animation to the drawer
        //this.allowArrowAnimation();

        // On back button, return to first section of the drawer. It's buggy right now:
        // https://github.com/neokree/MaterialNavigationDrawer/issues/33#issuecomment-69727925
        // this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        //create sections
        this.addSection(newSection("Schedule", new TalkTabsFragment()).setSectionColor(Color.parseColor(FullStackFestConfig.BARUCO_COLOR)));
        this.addSection(newSection("Speakers", new SpeakerListFragment()).setSectionColor(Color.parseColor(FullStackFestConfig.FUTUREJS_COLOR)));

        // ---------------------------------------------------
        // set a subheader
        this.addSubheader("Subheader");

        //create sections
        this.addSection(newSection("Sponsors", new TalkListFragment()).setSectionColor(Color.parseColor("#cddc39")));
        this.addSection(newSection("Map", new SpeakerListFragment()).setSectionColor(Color.parseColor("#ff9800")));

        this.addBottomSection(newSection("About", new SpeakerListFragment()).setSectionColor(Color.parseColor("#455A64")));
    }
}
