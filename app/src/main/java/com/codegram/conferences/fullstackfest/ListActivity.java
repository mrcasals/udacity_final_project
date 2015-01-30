package com.codegram.conferences.fullstackfest;

import android.content.Intent;
import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class ListActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        //this.setDrawerHeaderImage(R.drawable.mat2);

        //create sections
        this.addSection(newSection("Schedule", new TalkListFragment()));
        this.addSection((newSection("Speakers", new SpeakerListFragment())));
    }
}
