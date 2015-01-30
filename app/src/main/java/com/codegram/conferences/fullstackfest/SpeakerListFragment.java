package com.codegram.conferences.fullstackfest;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codegram.conferences.fullstackfest.labs.SpeakerLab;
import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.models.Speaker;
import com.codegram.conferences.fullstackfest.models.Talk;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setTitle(R.string.talks_activity_title);

        mSpeakers = SpeakerLab.get(getActivity()).getSpeakers();

        ConferenceAdapter adapter = new ConferenceAdapter(mSpeakers);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ConferenceAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class ConferenceAdapter extends ArrayAdapter<Speaker> {
        public ConferenceAdapter(ArrayList<Speaker> speakers) {
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
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.speaker_list_item_name);
            titleTextView.setText(speaker.getName());
            return convertView;
        }
    }
}
