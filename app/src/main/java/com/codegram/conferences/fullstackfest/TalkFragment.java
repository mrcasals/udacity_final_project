package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codegram.conferences.fullstackfest.labs.SpeakerLab;
import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.models.Speaker;
import com.codegram.conferences.fullstackfest.models.Talk;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * TalkFragment.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link TalkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TalkFragment extends Fragment {
    public static final String EXTRA_TALK_ID =
            "com.codegram.conferences.fullstackfest.talk_id";

    private Talk mTalk;

    private TextView mTitleView;
    private TextView mSpeakerName;

    public static TalkFragment newInstance(int talkId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TALK_ID, talkId);

        TalkFragment fragment = new TalkFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int talkId = (int)getArguments().getSerializable(EXTRA_TALK_ID);

        mTalk = TalkLab.get(getActivity()).getTalk(talkId);

        int color;
        if(talkId % 2 == 0) {
            color = Color.parseColor("#009688");
        } else {
            color = Color.parseColor("#FFC107");
        }

        ((SingleFragmentActivity)getActivity()).setToolbarColor(color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_talk, container, false);

        mTitleView = (TextView)v.findViewById(R.id.talk_title);
        mTitleView.setText(mTalk.getTitle());

        Speaker speaker = SpeakerLab.get(getActivity()).getSpeaker(mTalk.getSpeakerId());

        mSpeakerName = (TextView)v.findViewById(R.id.speaker_name);
        mSpeakerName.setText(speaker.getName());

        return v;
    }
}
