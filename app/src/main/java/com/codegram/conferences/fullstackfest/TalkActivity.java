package com.codegram.conferences.fullstackfest;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by marc on 01/02/15.
 */
public class TalkActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        int talkId = (int)getIntent()
                .getSerializableExtra(TalkFragment.EXTRA_TALK_ID);
        return TalkFragment.newInstance(talkId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.no_change, R.animator.push_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.animator.no_change, R.animator.push_down);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected int getActivityLayoutId() {
        return R.layout.activity_talk;
    }
}
