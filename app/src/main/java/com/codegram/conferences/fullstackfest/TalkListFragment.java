package com.codegram.conferences.fullstackfest;

import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codegram.conferences.fullstackfest.adapters.TalkListAdapter;
import com.codegram.conferences.fullstackfest.data.DatabaseContract;
import com.codegram.conferences.fullstackfest.labs.SpeakerLab;
import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.models.Speaker;
import com.codegram.conferences.fullstackfest.models.Talk;
import com.codegram.conferences.fullstackfest.tasks.FetchDataTask;
import com.makeramen.RoundedImageView;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the OnFragmentInteractionListener
 * interface.
 */
public class TalkListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int TALKS_LOADER = 0;

    public static final String EXTRA_TALKS_TAG =
            "com.codegram.conferences.fullstackfest.talks_tag";

    private final static String[] TALK_LIST_COLUMNS = {
            DatabaseContract.TalkEntry.TABLE_NAME + "." + DatabaseContract.TalkEntry._ID,
            DatabaseContract.TalkEntry.COLUMN_TITLE,
            DatabaseContract.TalkEntry.COLUMN_DESCRIPTION,
            DatabaseContract.TalkEntry.COLUMN_TAGS,
            DatabaseContract.SpeakerEntry.TABLE_NAME + "." + DatabaseContract.SpeakerEntry._ID,
            DatabaseContract.SpeakerEntry.COLUMN_TALK_ID,
            DatabaseContract.SpeakerEntry.COLUMN_NAME,
            DatabaseContract.SpeakerEntry.COLUMN_BIO,
            DatabaseContract.SpeakerEntry.COLUMN_PHOTO_URL
    };

    public static final int COL_TALK_ID = 0;
    public static final int COL_TALK_TITLE = 1;
    public static final int COL_TALK_DESCRIPTION = 2;
    public static final int COL_TALK_TAGS = 3;
    public static final int COL_SPEAKER_ID = 4;
    public static final int COL_SPEAKER_TALK_ID = 5;
    public static final int COL_SPEAKER_NAME = 6;
    public static final int COL_SPEAKER_BIO = 7;
    public static final int COL_SPEAKER_PHOTO_URL = 8;

    TalkListAdapter mAdapter;
    String mTalkTag;

    public static TalkListFragment newInstance(String talkTag) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TALKS_TAG, talkTag);

        TalkListFragment fragment = new TalkListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conference_list, container, false);

        mTalkTag = (String) getArguments().getSerializable(EXTRA_TALKS_TAG);

        mAdapter = new TalkListAdapter(getActivity(), null, 0);
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_conference_list_id);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView l, View v, int position, long id) {
                // We get the talk we've clicked on
                Cursor cursor = (Cursor) l.getItemAtPosition(position);
                if (cursor != null) {

                    // We create an Intent to link the current activity with the details one
                    Intent intent = new Intent(getActivity(), TalkActivity.class);
                    // As an extra, we put the ID of the talk we want to show
                    intent.putExtra(TalkFragment.EXTRA_TALK_ID, cursor.getInt(COL_TALK_ID));

                    // We start the other activity.
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.animator.push_up, R.animator.no_change);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_talks_list, menu);
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
        FetchDataTask fetchDataTask = new FetchDataTask(getActivity());
        fetchDataTask.execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TALKS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(TALKS_LOADER, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = DatabaseContract.TalkEntry.TABLE_NAME + "." + DatabaseContract.TalkEntry._ID + " ASC";
        Uri talksUri = DatabaseContract.TalkEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                talksUri,
                TALK_LIST_COLUMNS,
                "talks.tags LIKE ?",
                new String[] {"%" + mTalkTag + "%"},
                sortOrder
                );
    }
}
