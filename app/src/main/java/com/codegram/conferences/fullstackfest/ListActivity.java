package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class ListActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        // Set custom background image for the header
        this.setDrawerHeaderImage(R.drawable.mat3);

        // Don't show the sidebar on app opening
        this.disableLearningPattern();

        // On back button, return to first section of the drawer. It's buggy right now:
        // https://github.com/neokree/MaterialNavigationDrawer/issues/33#issuecomment-69727925
        // this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        // to disable the menu opening every time we open the app
        // this.disableLearningPattern();

        //create sections
        this.addSection(newSection("Schedule", new TalkListFragment()).setSectionColor(Color.parseColor("#9c27b0")));
        this.addSection((newSection("Speakers", new SpeakerListFragment())).setSectionColor(Color.parseColor("#03a9f4")));

        // ---------------------------------------------------
        // set a subheader
        this.addSubheader("Subheader");

        //create sections
        this.addSection(newSection("Sponsors", new TalkListFragment()).setSectionColor(Color.parseColor("#cddc39")));
        this.addSection((newSection("Map", new SpeakerListFragment())).setSectionColor(Color.parseColor("#ff9800")));
    }
}
