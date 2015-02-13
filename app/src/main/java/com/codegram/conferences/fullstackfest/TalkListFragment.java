package com.codegram.conferences.fullstackfest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
public class TalkListFragment extends ListFragment {

    private ArrayList<Talk> mTalks = new ArrayList<Talk>();
    TalkListAdapter mAdapter;
    private Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.BLACK)
            .borderWidthDp(1)
            .cornerRadiusDp(90)
            .oval(false)
            .build();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setTitle(R.string.talks_activity_title);

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
    }

    private void updateRemoteData() {
        FetchDataTask fetchDataTask = new FetchDataTask(getActivity(), mAdapter);
        fetchDataTask.execute();
        mTalks = TalkLab.get(getActivity()).getTalks();
    }

    private class TalkListAdapter extends ArrayAdapter<Talk> {
        public TalkListAdapter(ArrayList<Talk> talks) {
            // required to properly hook up your dataset of Talks
            // 0 because we are not using a predefined layout, so 0 is OK
            super(getActivity(), 0, talks);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_talk, null);
            }

            Talk talk = getItem(position);
            Speaker speaker = SpeakerLab.get(getActivity()).getSpeaker(talk.getSpeakerId());

            TextView primaryTextView =
                    (TextView)convertView.findViewById(R.id.talk_list_item_primary);
            TextView secondaryTextView =
                    (TextView)convertView.findViewById(R.id.talk_list_item_secondary_1);
            TextView secondary2TextView =
                    (TextView)convertView.findViewById(R.id.talk_list_item_secondary_2);
            RoundedImageView avatarView =
                    (RoundedImageView)convertView.findViewById(R.id.talk_list_item_avatar);

            primaryTextView.setText(speaker.getName());
            secondaryTextView.setText(talk.getTitle());
            secondary2TextView.setText("9:00");

            avatarView.setImageDrawable(null);
            Picasso picasso = Picasso.with(getActivity());
            //picasso.setIndicatorsEnabled(true); // Needs moving RoundedImageView to ImageView (also in layout)
            //picasso.setLoggingEnabled(true);
            picasso.load(speaker.getPictureUrl())
                    .fit()
                    .transform(transformation)
                    .into(avatarView);
            return convertView;
        }
    }
}
