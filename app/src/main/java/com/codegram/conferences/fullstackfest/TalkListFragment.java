package com.codegram.conferences.fullstackfest;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.talks_activity_title);

        mTalks = TalkLab.get(getActivity()).getTalks();

        ConferenceAdapter adapter = new ConferenceAdapter(mTalks);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ConferenceAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class ConferenceAdapter extends ArrayAdapter<Talk> {
        public ConferenceAdapter(ArrayList<Talk> talks) {
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
