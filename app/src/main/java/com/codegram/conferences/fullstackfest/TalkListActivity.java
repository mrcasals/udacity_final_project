package com.codegram.conferences.fullstackfest;

import android.support.v4.app.Fragment;



public class TalkListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() { return new TalkListFragment(); }
}
