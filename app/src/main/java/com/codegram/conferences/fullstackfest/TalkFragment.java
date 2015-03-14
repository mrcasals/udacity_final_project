package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

    private LinearLayout mTalkData;
    private LinearLayout mHeaderTalkDetails;

    private ImageView mSpeakerAvatar;
    private Toolbar mToolbarView;
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

        Speaker speaker = SpeakerLab.get(getActivity()).getSpeaker(mTalk.getSpeakerId());

        // find header talk details views
        mHeaderTalkDetails = (LinearLayout)getActivity().findViewById(R.id.header_talk_details);
        LinearLayout headerTalkData = (LinearLayout)getActivity().findViewById(R.id.header_talk_title_data);
        TextView headerTalkTitle = (TextView)getActivity().findViewById(R.id.header_talk_title);
        //TextView headerTalkTime = (TextView)getActivity().findViewById(R.id.header_talk_time);

        // find talk details views
        mTitleView = (TextView)v.findViewById(R.id.talk_title);
        mTalkData = (LinearLayout)v.findViewById(R.id.talk_title_data);

        // find other views
        mSpeakerName = (TextView)v.findViewById(R.id.speaker_name);
        mSpeakerAvatar = (ImageView)v.findViewById(R.id.talk_avatar);
        mTalkDescription = (TextView)v.findViewById(R.id.talk_description);
        TextView speakerBio = (TextView)v.findViewById(R.id.speaker_bio);


        // find toolbar and scrollview
        mToolbarView = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((SingleFragmentActivity)getActivity()).setSupportActionBar(mToolbarView);
        mScrollView = (ObservableScrollView) v.findViewById(R.id.talk_scroll);

        // fill views

        mTitleView.setText(mTalk.getTitle());
        headerTalkTitle.setText(mTalk.getTitle());

        mTalkData.setBackground(new ColorDrawable(FullStackFestConfig.getConfColor(mTalk)));
        headerTalkData.setBackground(new ColorDrawable(FullStackFestConfig.getConfColor(mTalk)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = ((SingleFragmentActivity)getActivity()).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(FullStackFestConfig.getConfColor(mTalk));
        }
        
        mSpeakerName.setText(speaker.getName());
        Picasso picasso = Picasso.with(getActivity());
        picasso.load(speaker.getPictureUrl())
                .fit()
                .centerCrop()
                .into(mSpeakerAvatar);
        mTalkDescription.setText(Html.fromHtml(mTalk.getDescription()));
        speakerBio.setText(Html.fromHtml(speaker.getBio()));
        speakerBio.setMovementMethod(LinkMovementMethod.getInstance());

        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, FullStackFestConfig.getConfColor(mTalk)));
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.talk_avatar_height) -
            getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

        return v;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = FullStackFestConfig.getConfColor(mTalk);
        float alpha = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));

        // Speaker avatar parallax effect
        mSpeakerAvatar.setTranslationY(scrollY / 2);

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
            mHeaderTalkDetails.setVisibility(View.VISIBLE);
        } else {
            mHeaderTalkDetails.setVisibility(View.GONE);
        }
    }

    private void setActionBarBackgroundColor() {
        ((SingleFragmentActivity)getActivity()).setToolbarColor(FullStackFestConfig.getConfColor(mTalk));
    }
}
