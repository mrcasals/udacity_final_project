package com.codegram.conferences.fullstackfest;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.codegram.conferences.fullstackfest.data.DatabaseContract;
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
public class TalkFragment extends Fragment implements ObservableScrollViewCallbacks, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_TALK_ID =
            "com.codegram.conferences.fullstackfest.talk_id";

    public static final int DETAIL_LOADER = 0;

    private final static String[] TALK_DETAILS_COLUMNS = {
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

    private View parentView;

    private Talk mTalk;
    private int mTalkId;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTalkId = (int)getArguments().getSerializable(EXTRA_TALK_ID);
        parentView = inflater.inflate(R.layout.fragment_talk, container, false);
        return parentView;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()) return;

        mTalk = buildTalk(data);

        Speaker speaker = buildSpeaker(data);

        // find header talk details views
        mHeaderTalkDetails = (LinearLayout)getActivity().findViewById(R.id.header_talk_details);
        LinearLayout headerTalkData = (LinearLayout)getActivity().findViewById(R.id.header_talk_title_data);
        TextView headerTalkTitle = (TextView)getActivity().findViewById(R.id.header_talk_title);
        TextView headerTalkTime = (TextView)getActivity().findViewById(R.id.header_talk_time);

        // find talk details views
        mTitleView = (TextView)parentView.findViewById(R.id.talk_title);
        TextView talkTime = (TextView)parentView.findViewById(R.id.talk_time);
        mTalkData = (LinearLayout)parentView.findViewById(R.id.talk_title_data);

        // find other views
        mSpeakerName = (TextView)parentView.findViewById(R.id.speaker_name);
        mSpeakerAvatar = (ImageView)parentView.findViewById(R.id.talk_avatar);
        mTalkDescription = (TextView)parentView.findViewById(R.id.talk_description);
        TextView speakerBio = (TextView)parentView.findViewById(R.id.speaker_bio);


        // find toolbar and scrollview
        mToolbarView = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((SingleFragmentActivity)getActivity()).setSupportActionBar(mToolbarView);
        mScrollView = (ObservableScrollView) parentView.findViewById(R.id.talk_scroll);

        // fill views
        mTitleView.setText(mTalk.getTitle());
        headerTalkTitle.setText(mTalk.getTitle());

        talkTime.setText(speaker.getName());
        headerTalkTime.setText(speaker.getName());

        mTalkData.setBackground(new ColorDrawable(FullStackFestConfig.getConfColor(mTalk)));
        headerTalkData.setBackground(new ColorDrawable(FullStackFestConfig.getConfColor(mTalk)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = ((SingleFragmentActivity)getActivity()).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(FullStackFestConfig.getConfDarkColor(mTalk));
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

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.talk_avatar_height);

        mToolbarView.setVisibility(View.GONE);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if(intent == null)
            return null;

        return new CursorLoader(getActivity(),
                DatabaseContract.TalkEntry.CONTENT_URI,
                TALK_DETAILS_COLUMNS,
                "talks._id = ?",
                new String[]{Integer.toString(mTalkId)},
                null
        );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    private Talk buildTalk(Cursor cursor) {
        return new Talk(
                cursor.getInt(TalkFragment.COL_TALK_ID),
                cursor.getString(TalkFragment.COL_TALK_TITLE),
                cursor.getString(TalkFragment.COL_TALK_DESCRIPTION),
                cursor.getString(TalkFragment.COL_TALK_TAGS)
        );
    }

    private Speaker buildSpeaker(Cursor cursor) {
        return new Speaker(
                cursor.getInt(TalkFragment.COL_SPEAKER_ID),
                cursor.getInt(TalkFragment.COL_SPEAKER_TALK_ID),
                cursor.getString(TalkFragment.COL_SPEAKER_NAME),
                cursor.getString(TalkFragment.COL_SPEAKER_PHOTO_URL),
                cursor.getString(TalkFragment.COL_SPEAKER_BIO)
        );
    }
}
