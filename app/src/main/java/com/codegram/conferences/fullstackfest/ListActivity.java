package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class ListActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        // Set custom background image for the header
        this.setDrawerHeaderImage(R.drawable.mat3);

        // to disable the menu opening every time we open the app
        // this.disableLearningPattern();

        //create sections
        this.addSection(newSection("Schedule", new TalkListFragment()).setSectionColor(Color.parseColor("#9c27b0")));
        this.addSection((newSection("Speakers", new SpeakerListFragment())).setSectionColor(Color.parseColor("#03a9f4")));

        // ---------------------------------------------------
        // set a subheader
        this.addSubheader("Subheader 2");

        //create sections
        this.addSection(newSection("Schedule", new TalkListFragment()).setSectionColor(Color.parseColor("#9c27b0")));
        this.addSection((newSection("Speakers", new SpeakerListFragment())).setSectionColor(Color.parseColor("#03a9f4")));
    }
}
