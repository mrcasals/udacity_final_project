package com.codegram.conferences.fullstackfest;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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
public class ConferenceListFragment extends ListFragment {

    private ArrayList<Conference> mConferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);

        mConferences = ConferenceLab.get(getActivity()).getConferences();

        ConferenceAdapter adapter = new ConferenceAdapter(mConferences);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ConferenceAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class ConferenceAdapter extends ArrayAdapter<Conference> {
        public ConferenceAdapter(ArrayList<Conference> conferences) {
            // required to properly hook up your dataset of Crimes
            // 0 because we are not using a predefined layout, so 0 is OK
            super(getActivity(), 0, conferences);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_conference, null);
            }

            Conference conference = getItem(position);
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.conference_list_item_title);
            titleTextView.setText(conference.getTitle());
            return convertView;
        }
    }
}
