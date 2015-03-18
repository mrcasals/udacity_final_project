package com.codegram.conferences.fullstackfest.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codegram.conferences.fullstackfest.R;
import com.codegram.conferences.fullstackfest.TalkListFragment;
import com.codegram.conferences.fullstackfest.models.Speaker;
import com.codegram.conferences.fullstackfest.models.Talk;
import com.makeramen.RoundedImageView;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by marcriera on 3/17/15.
 */
public class TalkListAdapter extends CursorAdapter {
    private Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.BLACK)
            .borderWidthDp(1)
            .cornerRadiusDp(90)
            .oval(false)
            .build();

    private Context mContext;

    public TalkListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_talk, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        Talk talk = buildTalk(cursor);
        Speaker speaker = buildSpeaker(cursor);

        TextView primaryTextView =
                (TextView)convertView.findViewById(R.id.talk_list_item_primary);
        TextView secondaryTextView =
                (TextView)convertView.findViewById(R.id.talk_list_item_secondary_1);
        RoundedImageView avatarView =
                (RoundedImageView)convertView.findViewById(R.id.talk_list_item_avatar);

        primaryTextView.setText(talk.getTitle());
        secondaryTextView.setText(speaker.getName());

        avatarView.setImageDrawable(null);
        Picasso picasso = Picasso.with(mContext);
        //picasso.setIndicatorsEnabled(true); // Needs moving RoundedImageView to ImageView (also in layout)
        //picasso.setLoggingEnabled(true);
        picasso.load(speaker.getPictureUrl())
                .fit()
                .transform(transformation)
                .into(avatarView);
    }

    private Talk buildTalk(Cursor cursor) {
        return new Talk(
                cursor.getInt(TalkListFragment.COL_TALK_ID),
                cursor.getString(TalkListFragment.COL_TALK_TITLE),
                cursor.getString(TalkListFragment.COL_TALK_DESCRIPTION),
                cursor.getString(TalkListFragment.COL_TALK_TAGS)
        );
    }

    private Speaker buildSpeaker(Cursor cursor) {
        return new Speaker(
                cursor.getInt(TalkListFragment.COL_SPEAKER_ID),
                cursor.getInt(TalkListFragment.COL_SPEAKER_TALK_ID),
                cursor.getString(TalkListFragment.COL_SPEAKER_NAME),
                cursor.getString(TalkListFragment.COL_SPEAKER_PHOTO_URL),
                cursor.getString(TalkListFragment.COL_SPEAKER_BIO)
        );
    }
}
