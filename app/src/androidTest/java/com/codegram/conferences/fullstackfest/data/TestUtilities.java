package com.codegram.conferences.fullstackfest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by marcriera on 3/12/15.
 */
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createSpeakerValues(long talkId) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(DatabaseContract.SpeakerEntry.COLUMN_NAME, "Aaron Patterson");
        testValues.put(DatabaseContract.SpeakerEntry.COLUMN_BIO, "Cat lover, ninja ballet dancer");
        testValues.put(DatabaseContract.SpeakerEntry.COLUMN_PHOTO_URL, "http://yeah.com/test.jpg");
        testValues.put(DatabaseContract.SpeakerEntry.COLUMN_TALK_ID, talkId);


        return testValues;
    }

    static ContentValues createTalkValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(DatabaseContract.TalkEntry.COLUMN_TITLE, "Talk Title");
        testValues.put(DatabaseContract.TalkEntry.COLUMN_DESCRIPTION, "Talk Description");

        return testValues;
    }

    static long insertTalkValues(Context context) {
        // insert our test records into the database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTalkValues();

        long talkRowId;
        talkRowId = db.insert(DatabaseContract.TalkEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Talk Values", talkRowId != -1);

        return talkRowId;
    }
}
