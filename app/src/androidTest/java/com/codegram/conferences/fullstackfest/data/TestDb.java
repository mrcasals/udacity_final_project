package com.codegram.conferences.fullstackfest.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by marcriera on 3/12/15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(DatabaseContract.TalkEntry.TABLE_NAME);
        tableNameHashSet.add(DatabaseContract.SpeakerEntry.TABLE_NAME);

        mContext.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        SQLiteDatabase db = new DatabaseHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the speakers entry
        // and talks entry tables
        assertTrue("Error: Your database was created without both the speakers entry and talks entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + DatabaseContract.TalkEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> talkColumnHashSet = new HashSet<String>();
        talkColumnHashSet.add(DatabaseContract.TalkEntry._ID);
        talkColumnHashSet.add(DatabaseContract.TalkEntry.COLUMN_TITLE);
        talkColumnHashSet.add(DatabaseContract.TalkEntry.COLUMN_DESCRIPTION);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            talkColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required talk
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required talk entry columns",
                talkColumnHashSet.isEmpty());
        db.close();
    }

    public void testTalkTable() {
        insertTalk();
    }


    public void testSpeakerTable() {
        // First insert the speaker, and then use the speakerRowId to insert
        // the talk. Make sure to cover as many failure cases as you can.

        long talkRowId = insertTalk();

        // First step: Get reference to writable database
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)
        ContentValues talkValues = TestUtilities.createSpeakerValues(talkRowId);

        // Insert ContentValues into database and get a row ID back
        long speakerRowId = db.insert(DatabaseContract.SpeakerEntry.TABLE_NAME, null, talkValues);
        assertTrue(speakerRowId != -1);

        // Query the database and receive a Cursor back
        Cursor speakerCursor = db.query(
                DatabaseContract.SpeakerEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Move the cursor to a valid database row
        assertTrue("Error: No records found in speakers table", speakerCursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: speaker entry failed to validate",
                speakerCursor, talkValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from speaker query",
                speakerCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        speakerCursor.close();
        dbHelper.close();
    }


    public long insertTalk() {
        // First step: Get reference to writable database
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createSpeakerValues if you wish)
        ContentValues testValues = TestUtilities.createTalkValues();

        // Insert ContentValues into database and get a row ID back
        long talkRowId = db.insert(DatabaseContract.TalkEntry.TABLE_NAME, null, testValues);

        // Query the database and receive a Cursor back
        assertTrue(talkRowId != -1);

        // Move the cursor to a valid database row
        Cursor cursor = db.query(
                DatabaseContract.TalkEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: No records returned from talks query", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Talk Query Validation failed",
                cursor, testValues);

        // Move the cursor to validate there's only one record
        assertFalse("Error: More than one record returned from talk query",
                cursor.moveToNext());

        // Finally, close the cursor and database
        cursor.close();
        db.close();
        return talkRowId;
    }
}