package com.codegram.conferences.fullstackfest;

import android.support.v4.app.Fragment;

/**
 * Created by marc on 01/02/15.
 */
public class TalkActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        int talkId = (int)getIntent()
                .getSerializableExtra(TalkFragment.EXTRA_TALK_ID);
        return TalkFragment.newInstance(talkId);    }
}
