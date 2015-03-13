package com.codegram.conferences.fullstackfest.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by marcriera on 3/13/15.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final int TALK_ID = 1;

    // content://com.example.android.sunshine.app/talks"
    private static final Uri TEST_TALKS_DIR = DatabaseContract.TalkEntry.CONTENT_URI;
    private static final Uri TEST_TALK_ITEM = DatabaseContract.TalkEntry.buildTalkUri(TALK_ID);
    private static final Uri TEST_SPEAKER_WITH_TALK_ITEM = DatabaseContract.SpeakerEntry.buildSpeakerTalk(TALK_ID);
    // content://com.example.android.sunshine.app/speakers"
    private static final Uri TEST_SPEAKERS_DIR = DatabaseContract.SpeakerEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = DatabaseProvider.buildUriMatcher();

        assertEquals("Error: The TALKS URI was matched incorrectly.",
                testMatcher.match(TEST_TALKS_DIR), DatabaseProvider.TALKS);
        assertEquals("Error: The TALK URI was matched incorrectly.",
                testMatcher.match(TEST_TALK_ITEM), DatabaseProvider.TALK);
        assertEquals("Error: The TALK WITH SPEAKER URI was matched incorrectly.",
                testMatcher.match(TEST_SPEAKER_WITH_TALK_ITEM), DatabaseProvider.SPEAKER_WITH_TALK);
        assertEquals("Error: The SPEAKERS URI was matched incorrectly.",
                testMatcher.match(TEST_SPEAKERS_DIR), DatabaseProvider.SPEAKERS);
    }
}
