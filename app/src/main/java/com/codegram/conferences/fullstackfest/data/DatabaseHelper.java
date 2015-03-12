package com.codegram.conferences.fullstackfest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.codegram.conferences.fullstackfest.data.DatabaseContract.SpeakerEntry;
import com.codegram.conferences.fullstackfest.data.DatabaseContract.TalkEntry;

/**
 * Created by marcriera on 3/12/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "fullstackfest.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TALKS_TABLE = "CREATE TABLE " + TalkEntry.TABLE_NAME + " (" +
                TalkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TalkEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TalkEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                TalkEntry.COLUMN_SPEAKER_ID + " INTEGER NOT NULL, " +
                // Set up the location column as a foreign key to location table.
                "FOREIGN KEY (" + TalkEntry.COLUMN_SPEAKER_ID + ") REFERENCES " +
                SpeakerEntry.TABLE_NAME + " (" + SpeakerEntry._ID + ")" +
                " );";

        final String SQL_CREATE_SPEAKERS_TABLE = "CREATE TABLE " + SpeakerEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                SpeakerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                SpeakerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                SpeakerEntry.COLUMN_BIO + " TEXT NOT NULL, " +
                SpeakerEntry.COLUMN_PHOTO_URL + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_TALKS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SPEAKERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SpeakerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TalkEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
