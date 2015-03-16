package com.codegram.conferences.fullstackfest.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by marcriera on 3/12/15.
 */
public class DatabaseProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    static final int SPEAKERS = 100;
    static final int SPEAKER_WITH_TALK = 101;
    static final int TALKS = 200;
    static final int TALK = 201;

    private static final String sTalkSelection =
            DatabaseContract.TalkEntry.TABLE_NAME +
                    "." + DatabaseContract.TalkEntry._ID + " = ?";

    private static final String sSpeakerSelection =
            DatabaseContract.SpeakerEntry.TABLE_NAME +
                    "." + DatabaseContract.SpeakerEntry._ID + " = ?";

    private static final String sSpeakerByTalkIdSelection =
            DatabaseContract.SpeakerEntry.TABLE_NAME +
                    "." + DatabaseContract.SpeakerEntry.COLUMN_TALK_ID + " = ?";

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        matcher.addURI(authority, DatabaseContract.PATH_TALKS, TALKS);
        matcher.addURI(authority, DatabaseContract.PATH_TALKS + "/#", TALK);
        matcher.addURI(authority, DatabaseContract.PATH_TALKS + "/#/speaker", SPEAKER_WITH_TALK);

        matcher.addURI(authority, DatabaseContract.PATH_SPEAKERS, SPEAKERS);
        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case TALK:
                return DatabaseContract.TalkEntry.CONTENT_ITEM_TYPE;
            case TALKS:
                return DatabaseContract.TalkEntry.CONTENT_TYPE;
            case SPEAKER_WITH_TALK:
                return DatabaseContract.SpeakerEntry.CONTENT_ITEM_TYPE;
            case SPEAKERS:
                return DatabaseContract.SpeakerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(selection == null)
            selection = "1";

        switch (match) {
            case TALKS: {
                rowsDeleted = db.delete(DatabaseContract.TalkEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case SPEAKERS: {
                rowsDeleted = db.delete(DatabaseContract.SpeakerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TALK: {
                rowsUpdated = db.update(DatabaseContract.TalkEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case SPEAKER_WITH_TALK: {
                rowsUpdated = db.update(DatabaseContract.SpeakerEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TALKS: {
                long _id = db.insert(DatabaseContract.TalkEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DatabaseContract.TalkEntry.buildTalkUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SPEAKER_WITH_TALK: {
                long _id = db.insert(DatabaseContract.SpeakerEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DatabaseContract.SpeakerEntry.buildSpeakerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "talks/#/speaker"
            case SPEAKER_WITH_TALK:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.TalkEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "talks/#"
            case TALK: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.TalkEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "talks"
            case TALKS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.TalkEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "speakers"
            case SPEAKERS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.SpeakerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
