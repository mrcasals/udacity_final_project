package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codegram.conferences.fullstackfest.config.FullStackFestConfig;
import com.codegram.conferences.fullstackfest.labs.SpeakerLab;
import com.codegram.conferences.fullstackfest.labs.TalkLab;
import com.codegram.conferences.fullstackfest.models.Speaker;
import com.codegram.conferences.fullstackfest.models.Talk;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * TalkFragment.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link TalkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TalkFragment extends Fragment implements ObservableScrollViewCallbacks {
    public static final String EXTRA_TALK_ID =
            "com.codegram.conferences.fullstackfest.talk_id";

    private Talk mTalk;

    private TextView mTitleView;
    private TextView mSpeakerName;
    private TextView mTalkDescription;

    private LinearLayout mTalkHeader;
    private LinearLayout mTalkData;
    private LinearLayout mTalkDetailsContainer;

    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setActionBarBackgroundColor();

        View v = inflater.inflate(R.layout.fragment_talk, container, false);

        mTalkDetailsContainer = (LinearLayout)v.findViewById(R.id.talk_details_container);

        mTitleView = (TextView)v.findViewById(R.id.talk_title);
        mTitleView.setText(mTalk.getTitle());

        Speaker speaker = SpeakerLab.get(getActivity()).getSpeaker(mTalk.getSpeakerId());

        mSpeakerName = (TextView)v.findViewById(R.id.speaker_name);
        mSpeakerName.setText(speaker.getName());

        ImageView speakerAvatar = (ImageView)v.findViewById(R.id.talk_avatar);
        Picasso picasso = Picasso.with(getActivity());
        picasso.load(speaker.getPictureUrl())
                .fit()
                .centerCrop()
                .into(speakerAvatar);

        mTalkHeader = (LinearLayout)v.findViewById(R.id.talk_header);

        mTalkData = (LinearLayout)v.findViewById(R.id.talk_title_data);
        mTalkData.setBackground(new ColorDrawable(getConfColor()));

        mTalkDescription = (TextView)v.findViewById(R.id.talk_description);
        mTalkDescription.setText(Html.fromHtml(mTalk.getDescription()));

        TextView speakerBio = (TextView)v.findViewById(R.id.speaker_bio);
        speakerBio.setText(Html.fromHtml(speaker.getBio()));
        speakerBio.setMovementMethod(LinkMovementMethod.getInstance());

        mToolbarView = getActivity().findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getConfColor()));

        mScrollView = (ObservableScrollView) v.findViewById(R.id.talk_scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.talk_avatar_height) -
            getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

        return v;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getConfColor();
        float alpha = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        //ViewHelper.setTranslationY(mImageView, scrollY / 2);

        if(mTalk.getId() < 3)
            handleTalkDetailsScroll(scrollY);

    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void handleTalkDetailsScroll(int scrollY) {
        if(scrollY > mParallaxImageHeight) {
            //mTalkData.setTranslationY(getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material));
            ((LinearLayout)mTalkData.getParent()).removeView(mTalkData);
            ((LinearLayout)mToolbarView.getParent()).addView(mTalkData);
            mTalkDetailsContainer.setTranslationY(mTalkData.getHeight());
        } else {
            ((LinearLayout)mTalkData.getParent()).removeView(mTalkData);
            ((LinearLayout)mTalkHeader).addView(mTalkData);
            mTalkDetailsContainer.setTranslationY(0);
        }
    }

    private void setActionBarBackgroundColor() {
        ((SingleFragmentActivity)getActivity()).setToolbarColor(getConfColor());
    }

    private int getConfColor() {
        int color;
        if(mTalk.getId() % 2 == 0) {
            color = Color.parseColor(FullStackFestConfig.BARUCO_COLOR);
        } else {
            color = Color.parseColor(FullStackFestConfig.FUTUREJS_COLOR);
        }

        return color;
    }
}
