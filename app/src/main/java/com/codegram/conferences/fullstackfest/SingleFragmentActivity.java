package com.codegram.conferences.fullstackfest;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public abstract class SingleFragmentActivity extends ActionBarActivity{
    protected abstract Fragment createFragment();

    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentViewId());

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.frame_container, fragment)
                    .commit();
        }
    }

    public void setToolbarColor(int color) {
        mActionBar.setBackgroundDrawable(new ColorDrawable(color));
    }

    private int getActivityLayoutId() {
        return R.layout.activity_single_fragment;
    }

    private int getFragmentViewId() {
        return R.id.frame_container;
    }
}
