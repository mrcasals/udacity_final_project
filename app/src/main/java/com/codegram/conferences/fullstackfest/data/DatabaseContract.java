package com.codegram.conferences.fullstackfest.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by marcriera on 3/12/15.
 */
public class DatabaseContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.codegram.conferences.fullstackfest";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.codegram.conferences.fullstackfest/talks/ is a valid path for
    // looking at talks data. content://com.codegram.conferences.fullstackfest/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_TALKS = "talks";
    public static final String PATH_SPEAKERS = "speakers";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.setToNow();
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the talks table.
     */
    public static final class TalkEntry implements BaseColumns {
        public static final String TABLE_NAME = "talks";

        public static final String COLUMN_SPEAKER_ID = "speaker_id";

        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TALKS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TALKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TALKS;

        public static Uri buildTalkUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /*
        Inner class that defines the table contents of the speakers table
     */
    public static final class SpeakerEntry implements BaseColumns {

        public static final String TABLE_NAME = "speakers";

        // Column with the foreign key into the location table.
        public static final String COLUMN_NAME = "name";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_BIO = "bio";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_PHOTO_URL = "photo_url";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPEAKERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SPEAKERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SPEAKERS;


        public static Uri buildSpeakerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSpeakerTalk(String talkPath) {
            return BASE_CONTENT_URI.buildUpon().appendPath("/talks").appendPath(talkPath).appendPath("/speaker").build();
        }
    }
}
