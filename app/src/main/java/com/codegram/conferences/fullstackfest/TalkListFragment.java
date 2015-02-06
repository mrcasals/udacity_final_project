package com.codegram.conferences.fullstackfest;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.models.Talk;
import com.codegram.conferences.fullstackfest.tasks.FetchDataTask;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TalkListFragment extends ListFragment {

    private ArrayList<Talk> mTalks;
    TalkListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setTitle(R.string.talks_activity_title);

        mTalks = new ArrayList<Talk>();

        Log.d("Bla ", Integer.toString(mTalks.size()));

        mAdapter = new TalkListAdapter(mTalks);
        setListAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TalkListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRemoteData();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // We get the talk we've clicked on
        Talk talk = ((TalkListAdapter)getListAdapter()).getItem(position);

        // We create an Intent to link the current activity with the details one
        Intent intent = new Intent(getActivity(), TalkActivity.class);
        // As an extra, we put the ID of the talk we want to show
        intent.putExtra(TalkFragment.EXTRA_TALK_ID, talk.getId());
        // We start the other activity.
        startActivity(intent);
    }

    private void updateRemoteData() {
        FetchDataTask fetchDataTask = new FetchDataTask(getActivity(), mAdapter);
        fetchDataTask.execute();
        mTalks = TalkLab.get(getActivity()).getTalks();
    }

    public void updateTalks() {
        mTalks = TalkLab.get(getActivity()).getTalks();
    }

    private class TalkListAdapter extends ArrayAdapter<Talk> {
        public TalkListAdapter(ArrayList<Talk> talks) {
            // required to properly hook up your dataset of Crimes
            // 0 because we are not using a predefined layout, so 0 is OK
            super(getActivity(), 0, talks);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_talk, null);
            }

            Talk talk = getItem(position);
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.talk_list_item_title);
            titleTextView.setText(talk.getTitle());
            return convertView;
        }
    }
}
