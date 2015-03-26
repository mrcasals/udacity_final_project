package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codegram.conferences.fullstackfest.labs.SpeakerLab;
import com.codegram.conferences.fullstackfest.models.Speaker;
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
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SpeakerListFragment extends ListFragment {

    private ArrayList<Speaker> mSpeakers;
    private SpeakerListAdapter mAdapter;
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

        mSpeakers = SpeakerLab.get(getActivity()).getSpeakers();

        mAdapter = new SpeakerListAdapter(mSpeakers);
        setListAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SpeakerListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
    }

    private class SpeakerListAdapter extends ArrayAdapter<Speaker> {
        public SpeakerListAdapter(ArrayList<Speaker> speakers) {
            // required to properly hook up your dataset of Crimes
            // 0 because we are not using a predefined layout, so 0 is OK
            super(getActivity(), 0, speakers);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_speaker, null);
            }

            Speaker speaker = getItem(position);
            TextView nameTextView =
                    (TextView)convertView.findViewById(R.id.speaker_list_item_primary);
            RoundedImageView avatarView =
                    (RoundedImageView)convertView.findViewById(R.id.speaker_list_item_avatar);

            nameTextView.setText(speaker.getName());
            avatarView.setImageDrawable(null);
            Picasso.with(getActivity())
                    .load(speaker.getPictureUrl())
                    .fit()
                    .transform(transformation)
                    .into(avatarView);
            return convertView;
        }
    }
}
